package com.flinksight.flinkjob.sink;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.utils.Jsons;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**各通道Sink建议直接对接RESTful/SMTP/第三方SDK，便于后期灵活扩展与参数统一维护*/

public class FlinkAlertSink {
    public static void sink(DataStream<JobMetricsEventDTO> eventStream, String sinkType, String sinkParam, StreamExecutionEnvironment env) {
        DataStream<String> jsonStream = eventStream.map(Jsons::to);
        switch (sinkType.toLowerCase()) {
            case "kafka": case "es": case "hudi": case "pulsar": // ...已实现
                DynamicSinkFactory.applySink(jsonStream, sinkType, sinkParam, env); break;
            case "dingding":
                //jsonStream.addSink(new DingDingAlertSink(sinkParam)); break;
            case "wechat":
                jsonStream.addSink(new WeChatAlertSink(sinkParam)); break;
            case "sms":
                    //jsonStream.addSink(new SmsAlertSink(sinkParam)); break;
            case "email":
                //jsonStream.addSink(new EmailAlertSink(sinkParam)); break;
            default:
                throw new IllegalArgumentException("未知报警通道: " + sinkType);
        }
    }
}
