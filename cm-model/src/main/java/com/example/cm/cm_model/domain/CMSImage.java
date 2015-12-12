package com.example.cm.cm_model.domain;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Abstraction for image files
 */
@Entity
@Table(name = "CMSImage", uniqueConstraints = {})
public class CMSImage extends Updatable {

    private byte[] image;

    @NotNull
    private String filename;

    @NotNull
    private String contentType;

    @NotNull
    private long sizeInBytes;

    @Transient
    private boolean isValid;

//    public CMSImage()
//    {
//        this(null, null, 0L, null);
//    }
//
//    public CMSImage(String filename,
//                    byte[] image,
//                    long sizeInBytes,
//                    String contentType)
//    {
//        this.filename = filename;
//        this.contentType = contentType;
//        this.sizeInBytes = sizeInBytes;
//        this.image = image;
//    }

    public void build(MultipartFile multipartFile) throws IOException
    {
        if( multipartFile.getContentType().equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE) ||
            multipartFile.getContentType().equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE)) {

            this.setFilename(multipartFile.getOriginalFilename());
            this.setImage(multipartFile.getBytes());
            this.setSizeInBytes(multipartFile.getSize());
            this.setContentType(multipartFile.getContentType());
            this.setStatus(STATUS.PENDING);
            this.setValid(true);
        }
    }

    public boolean isValid()
    {
        return this.isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Transient
    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    @Override
    public String toString() {
        return "[CMSImage:" +
                "  id=" + this.getId() +
                "  createdBy=" + this.getCreatedBy() +
                "  createdDate=" + this.getCreatedDate() +
                "  filename=" + this.getFilename() +
                "  filename=" + this.getFilename() +
                ", contentType=" + this.getContentType() +
                ", sizeInBytes=" + this.getSizeInBytes() +
                ", valid=" + this.isValid() +
                "]";
    }
}
