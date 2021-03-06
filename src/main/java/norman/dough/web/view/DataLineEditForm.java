package norman.dough.web.view;

import norman.dough.domain.DataFile;
import norman.dough.domain.DataLine;
import norman.dough.exception.NotFoundException;
import norman.dough.service.DataFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DataLineEditForm {
    private DataFileService dataFileService;
    private Long id;
    private Integer version = 0;
    @NotNull(message = "Data File may not be blank.")
    private Long dataFileId;
    @NotNull(message = "Sequence may not be blank.")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer seq;
    @NotBlank(message = "Text may not be blank.")
    @Size(max = 255, message = "Text may not be over {max} characters long.")
    private String text;

    public DataLineEditForm() {
    }

    public DataLineEditForm(DataLine entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getDataFile() != null) {
            dataFileId = entity.getDataFile().getId();
        }
        seq = entity.getSeq();
        text = entity.getText();
    }

    public DataLine toEntity() throws NotFoundException {
        DataLine entity = new DataLine();
        entity.setId(id);
        entity.setVersion(version);
        if (dataFileId != null) {
            DataFile dataFile = dataFileService.findById(dataFileId);
            entity.setDataFile(dataFile);
        }
        entity.setSeq(seq);
        entity.setText(StringUtils.trimToNull(text));
        return entity;
    }

    public void setDataFileService(DataFileService dataFileService) {
        this.dataFileService = dataFileService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getDataFileId() {
        return dataFileId;
    }

    public void setDataFileId(Long dataFileId) {
        this.dataFileId = dataFileId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
