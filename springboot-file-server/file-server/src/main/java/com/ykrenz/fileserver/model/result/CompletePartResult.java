package com.ykrenz.fileserver.model.result;

import com.ykrenz.fileserver.entity.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ykren
 * @date 2022/3/7
 */

@ApiModel("上传结果")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletePartResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "不能为空")
    private String uploadId;

    @ApiModelProperty("file")
    private FileInfo file;

    public CompletePartResult(FileInfo file) {
        this.file = file;
    }
}
