package com.nhnacademy.minidooray.gateway.gateway.adaptor.impl;

import com.nhnacademy.minidooray.gateway.gateway.adaptor.TaskAdaptor;
import com.nhnacademy.minidooray.gateway.gateway.domain.Result;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TaskAdaptorImpl implements TaskAdaptor {
    private final RestTemplate restTemplate;

    @Override
    public Optional<List<TaskDto>> getTaskByProjectId(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<List<TaskDto>> exchange = restTemplate.exchange("http://localhost:8083/projects/{id}/tasks", HttpMethod.GET, entity, new ParameterizedTypeReference<List<TaskDto>>() {
        },id);
        return Optional.of(exchange.getBody());
    }

    @Override
    public Optional<TaskDto> registerTask(Long projectId,TaskRegister taskRegister) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(taskRegister,headers);
        ResponseEntity<TaskDto> exchange =
                restTemplate.exchange("http://localhost:8083/projects/{id}/tasks", HttpMethod.POST, entity, TaskDto.class, projectId);
        return Optional.of(exchange.getBody());
    }

    @Override
    public Optional<TaskDto> getTask(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<TaskDto> exchange =
                restTemplate.exchange("http://localhost:8083/projects/1/tasks/{id}", HttpMethod.GET, entity, TaskDto.class, id);
        return Optional.of(exchange.getBody());
    }

    @Override
    public Optional<Result> deleteTask(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Result> exchange =
                restTemplate.exchange("http://localhost:8083/projects/1/tasks/{id}",HttpMethod.DELETE, entity,Result.class,id);
        return Optional.of(exchange.getBody());
    }

    @Override
    public Optional<Result> updateTask(Long projectId, Long taskId, TaskRegister taskRegister) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(taskRegister,headers);
        ResponseEntity<Result> exchange =
                restTemplate.exchange("http://localhost:8083/projects/{projectId}/tasks/{taskId}",HttpMethod.PATCH, entity,Result.class,projectId,taskId);
        return Optional.of(exchange.getBody());
    }

}
