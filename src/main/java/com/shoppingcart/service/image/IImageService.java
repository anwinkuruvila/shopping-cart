package com.shoppingcart.service.image;

import com.shoppingcart.dto.ImageDto;
import com.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(long id);
    void deleteImageById(long id);
    List<ImageDto> addImage(List<MultipartFile> files, long productId);
    Image updateImage(MultipartFile file, long imageId);
}
