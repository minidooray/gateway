package com.nhnacademy.minidooray.gateway.gateway.service.projectMember;

import com.nhnacademy.minidooray.gateway.gateway.domain.Result;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectIdDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectMemberId;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectRegister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMemberService {
    List<ProjectIdDto> getProjectIdsByMemberId(String memberId);
    Result registerProject(Long projectId, String memberId);
    List<ProjectMemberId> getMemberIdsByProjectId(Long projectId);
}
