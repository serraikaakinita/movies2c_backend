package com.movies2c.backend.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MediaService {

    private final GridFsTemplate gridFsTemplate;

    public MediaService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String saveFile(MultipartFile file) throws IOException {
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return id.toHexString();
    }

    public GridFSFile findById(String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
    }

    public void deleteById(String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(id))));
    }
}
