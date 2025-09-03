package com.flinksight.common.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;


/**
* 将 Flink/Spark 的字符串 ID 稳定映射为 Long，方便与你的规则表(jobId 为 Long) 对齐。
* - 若是纯数字，直接解析；
* - 若是 16/32位十六进制，按无符号整型取低 63 位；
* - 否则用 CRC64 仿真（用 CRC32 组合）得到稳定 63 位。
*/
public class JobIdCodec {
public static Long toLongOrNull(String id){
if (id == null) return null;
try {
if (id.matches("^\\d+$")) return Long.parseLong(id);
if (id.matches("^[0-9a-fA-F]{16,64}$")) {
BigInteger bi = new BigInteger(id, 16);
return bi.and(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
}
} catch (Exception ignore) {}
// fallback: crc64 仿真（简化实现）
return pseudoCrc64(id);
}
private static long pseudoCrc64(String s){
CRC32 c1 = new CRC32(); CRC32 c2 = new CRC32();
byte[] b = s.getBytes(StandardCharsets.UTF_8);
int mid = b.length/2; c1.update(b,0,mid); c2.update(b,mid,b.length-mid);
long v = ((c1.getValue() & 0xffffffffL) << 31) ^ (c2.getValue() & 0x7fffffffL);
return (v & Long.MAX_VALUE);
}
}