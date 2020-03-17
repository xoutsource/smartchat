package com.xos.smartchat.controller;

import com.xos.smartchat.service.DataImportService;
import com.xos.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/dataimport", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataImportController {

    @Autowired
    private DataImportService dataImportService;

    @PostMapping("/createIndices")
    public Response createIndices() {
        try {
            dataImportService.createIndices();
            return Response.success(null);
        } catch (Throwable thr) {
            log.error(thr.getMessage(), thr);
            return Response.fail(thr.getMessage());
        }
    }

    @PostMapping("/syncEs")
    public Response syncEs() {
        try {
            dataImportService.syncEs();
            return Response.success(null);
        } catch (Throwable thr) {
            log.error(thr.getMessage(), thr);
            return Response.fail(thr.getMessage());
        }
    }
}
