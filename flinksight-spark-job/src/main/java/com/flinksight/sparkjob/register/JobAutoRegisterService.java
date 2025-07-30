package com.flinksight.sparkjob.register;

import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.common.utils.JsonUtil;
import okhttp3.*;

/**
 * 作业自动注册（平台自动发现）
 * 作业自动注册服务，Job启动时自动向主平台注册
 * 功能说明：每个新Spark Job上线时自动将自身注册到flinksight-backend（REST API/Kafka等），后端入库，无需人工维护。
 * SparkOpsJob.main() 启动时自动调用（url如 http://backend:port/api/job/register）
 * DTO JobRegisterRequest 字段包括 jobName、tenantId、operator、备注、traceId（用于平台唯一性约束和幂等
 */
public class JobAutoRegisterService {

    private static final OkHttpClient httpClient = new OkHttpClient();

    /**
     * 注册作业到flinksight-backend
     * @param registerUrl 平台注册API
     * @param req 作业注册请求
     */
    public static void registerJob(String registerUrl, JobRegisterRequestDTO req) throws Exception {
        RequestBody body = RequestBody.create(JsonUtil.toJson(req), MediaType.parse("application/json"));
        Request httpRequest = new Request.Builder().url(registerUrl).post(body).build();
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("作业注册失败, 状态码: " + response.code());
            }
        }
    }
}
