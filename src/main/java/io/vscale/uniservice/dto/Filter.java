package io.vscale.uniservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Dilyara Gabdreeva
 * 11-602
 * 25.05.2018
 */

@Builder
@Data
public class Filter {
    private String course;
    private String degree;
}
