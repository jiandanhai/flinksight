package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Node;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.enums.NodeState;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface NodeStructMapper extends GenericMapper<NodeDTO, Node> {
    /** NodeState -> Integer（DTO 需要 Integer） */
    default Integer map(NodeState value) {
        if (value == null) return null;
        // NodeState.code 是 "0"/"1"（字符串），这里安全转成 Integer
        try {
            return Integer.valueOf(value.getCode());
        } catch (NumberFormatException e) {
            // 码异常时返回 null，避免误判
            return null;
        }
    }

    /** Integer -> NodeState（反向 toEntity 用） */
    default NodeState map(Integer code) {
        if (code == null) return null;
        // 业务码仍用字符串表示
        NodeState s = NodeState.ofCodeOrNull(String.valueOf(code));
        // 你也可以按需要设默认：ofCodeOrDefault(String.valueOf(code), NodeState.DISABLED)
        return s;
    }

    /* 可选：如果你有其它 DTO/VO 里用到 String 状态码，也可以顺手补这两个 */
    default String mapToString(NodeState value) {
        return value == null ? null : value.getCode();
    }
    default NodeState mapFromString(String code) {
        return NodeState.ofCodeOrNull(code);
    }
}
