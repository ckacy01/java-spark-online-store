package org.technoready.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class WebSocketMessage {
        private String type;
        private String message;
        private Object data;
}
