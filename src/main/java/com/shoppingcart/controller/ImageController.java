package com.shoppingcart.controller;

import com.shoppingcart.dto.ImageDto;
import com.shoppingcart.exceptions.ResourceNotFoundException;
import com.shoppingcart.model.Image;
import com.shoppingcart.response.ApiResponse;
import com.shoppingcart.service.image.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/images")
public class ImageController {

    IImageService iImageService;

    @Autowired
    public ImageController(IImageService iImageService) {
        this.iImageService = iImageService;
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable(name = "imageId") long imageId) throws SQLException {

        Image image = iImageService.getImageById(imageId);
        ByteArrayResource byteArrayResource = new ByteArrayResource(image.getImage()
                .getBytes(1, (int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(byteArrayResource);
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable(name = "imageId") long imageId){
        try {
            Image image = iImageService.getImageById(imageId);
            if (image != null) {
                iImageService.deleteImageById( imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted!", null));
            }
        }catch (ResourceNotFoundException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("image id not found", re.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Image deletion failed!", null));
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable(name = "imageId")
                                                       long imageId, @RequestBody MultipartFile file){
        try {
            Image image = iImageService.getImageById(imageId);
            if (image != null) {
                iImageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Image updated", null));
            }
        }catch (ResourceNotFoundException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("image id not found", re.getMessage()));
        }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Image updated failed!", null));
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> images,
                                                 @RequestParam long productId){

        try {
            List<ImageDto> savedImages = iImageService.addImage(images, productId);
            return ResponseEntity.ok(new ApiResponse("Upload Complete!", savedImages));
        }
        catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occured", e.getMessage()));
        }
    }

}
