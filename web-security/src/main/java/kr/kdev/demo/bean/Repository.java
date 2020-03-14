package kr.kdev.demo.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 저장소
 */
@Data
public class Repository {
    private String id;
    @NotBlank private String name;
    private String description;
    private boolean isPublic = true;

    private String ownerId;

    private long created = System.currentTimeMillis();
    private long modified = System.currentTimeMillis();
}
