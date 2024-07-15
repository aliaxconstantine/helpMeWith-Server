package com.HelpMe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecodeFrom {
    public String key;
    public String value;
}
