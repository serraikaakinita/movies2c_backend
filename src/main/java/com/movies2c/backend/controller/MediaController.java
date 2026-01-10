package com.movies2c.backend.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.movies2c.backend.service.MediaService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;
    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    public MediaController(MediaService mediaService, GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket) {
        this.mediaService = mediaService;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String id = mediaService.saveFile(file);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> get(@PathVariable String id) throws IOException {
        GridFSFile file = mediaService.findById(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gridFSBucket.downloadToStream(new ObjectId(id), out);

        String contentType = file.getMetadata() != null ? file.getMetadata().getString("_contentType") : null;
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(out.toByteArray());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        mediaService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}
