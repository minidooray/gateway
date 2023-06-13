package com.nhnacademy.minidooray.gateway.gateway.adaptor;


import com.nhnacademy.minidooray.gateway.gateway.domain.Result;
import com.nhnacademy.minidooray.gateway.gateway.domain.comment.CommentDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.comment.CommentRegisterDto;

import java.util.List;
import java.util.Optional;

public interface CommentAdaptor {
    Optional<List<CommentDto>> getCommentsByTaskId(Long taskId);
    Optional<Result> registerCommentByTaskId(Long taskId, CommentRegisterDto commentRegisterDto);
}