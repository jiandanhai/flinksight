package com.flinksight.backend.exec.flink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlinkRestClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String submitJar(String baseUrl, String jarId, String entryClass,
                            Integer parallelism, String programArgs, Boolean allowNonRestoredState) throws Exception {
        String url = baseUrl + "/jars/" + jarId + "/run";
        var body = Map.of(
                "entryClass", entryClass,
                "programArgs", programArgs == null ? "" : programArgs,
                "parallelism", parallelism == null ? 1 : parallelism,
                "allowNonRestoredState", allowNonRestoredState != null && allowNonRestoredState
        );
        ResponseEntity<String> resp = restTemplate.postForEntity(url, body, String.class);
        ensure2xx(resp);
        JsonNode json = mapper.readTree(resp.getBody());
        return json.path("jobid").asText();
    }

    public String triggerSavepoint(String baseUrl, String jobId, String savepointDir, boolean drain) {
        String url = baseUrl + "/jobs/" + jobId + "/savepoints";
        var body = Map.of(
                "targetDirectory", savepointDir,
                "drain", drain
        );
        ResponseEntity<String> resp = restTemplate.postForEntity(url, body, String.class);
        ensure2xx(resp);
        return resp.getBody();
    }

    public void stopJob(String baseUrl, String jobId, boolean drain, String savepointDir) {
        // Flink >= 1.15: POST /jobs/:jobid/stop?drain=true
        String url = baseUrl + "/jobs/" + jobId + "/stop?drain=" + drain;
        if (savepointDir != null && !savepointDir.isBlank()) {
            url += "&targetDirectory=" + encode(savepointDir);
        }
        ResponseEntity<Void> resp = restTemplate.postForEntity(url, null, Void.class);
        ensure2xx(resp);
    }

    public void rescale(String baseUrl, String jobId, int parallelism) {
        String url = baseUrl + "/jobs/" + jobId + "/rescaling?parallelism=" + parallelism;
        ResponseEntity<Void> resp = restTemplate.patchForObject(url, null, ResponseEntity.class);
        // 某些版本不支持 PATCH；也可 fallback POST 到 /rescaling
    }

    public String uploadJar(String baseUrl, String filename, String base64Content) {
        String url = baseUrl + "/jars/upload";
        byte[] bin = Base64.getDecoder().decode(base64Content.getBytes(StandardCharsets.UTF_8));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("jarfile", new InMemoryFileResource(filename, bin));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = restTemplate.postForEntity(url, req, String.class);
        ensure2xx(resp);
        return resp.getBody(); // 返回 JSON，含 filename/jarId
    }

    private static void ensure2xx(ResponseEntity<?> resp) {
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Flink REST call failed: " + resp.getStatusCode() + " body=" + resp.getBody());
        }
    }

    private static String encode(String s) {
        return s.replace(" ", "%20");
    }
}