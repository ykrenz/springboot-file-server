package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "FileInfoRequest", description = "文件信息")
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "id")
    @NotBlank(message = "文件id")
    private String id;
}
