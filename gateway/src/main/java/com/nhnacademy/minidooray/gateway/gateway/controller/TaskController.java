package com.nhnacademy.minidooray.gateway.gateway.controller;

import com.nhnacademy.minidooray.gateway.gateway.domain.account.AccountDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectIdDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.project.ProjectMemberId;
import com.nhnacademy.minidooray.gateway.gateway.domain.tag.TagDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskModify;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskRegister;
import com.nhnacademy.minidooray.gateway.gateway.domain.task.TaskRegisterDto;
import com.nhnacademy.minidooray.gateway.gateway.domain.tasktag.TaskTagDto;
import com.nhnacademy.minidooray.gateway.gateway.exception.ProjectNotFoundException;
import com.nhnacademy.minidooray.gateway.gateway.service.account.AccountService;
import com.nhnacademy.minidooray.gateway.gateway.service.milestone.MileService;
import com.nhnacademy.minidooray.gateway.gateway.service.project.ProjectService;
import com.nhnacademy.minidooray.gateway.gateway.service.projectMember.ProjectMemberService;
import com.nhnacademy.minidooray.gateway.gateway.service.tag.TagService;
import com.nhnacademy.minidooray.gateway.gateway.service.task.TaskService;
import com.nhnacademy.minidooray.gateway.gateway.service.tasktag.TaskTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final ProjectMemberService memberService;
    private final AccountService accountService;
    private final MileService mileService;
    private final TagService tagService;
    private final TaskTagService taskTagService;

    @GetMapping("/project/{projectId}/task/register")
    public String viewTaskRegister(@PathVariable Long projectId, Model model){
        ProjectDto projectDto = null;
        try {
            projectDto = projectService.getProject(projectId);
        } catch (ProjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<ProjectMemberId> projectMemberIds = memberService.getMemberIdsByProjectId(projectId);
        List<AccountDto> projectMembers = new ArrayList<>();

        for(ProjectMemberId member:projectMemberIds){
            log.info(member.getAccountId());
            projectMembers.add(accountService.getAccount(member.getAccountId()));
        }
        model.addAttribute("Tags",tagService.getTagsByProjectId(projectId));
        model.addAttribute("Milestones",mileService.getMilestonesByProjectId(projectId));
        model.addAttribute("Project",projectDto);
        model.addAttribute("allAccounts",projectMembers);

        return "taskcreate";
    }
    @PostMapping("project/task/register")
    public String taskRegister(@ModelAttribute TaskRegisterDto registerDto,HttpSession session){
        //task 등록
        //task tag 연결
        TaskRegister taskRegister = new TaskRegister(registerDto.getTitle(),registerDto.getContent(),session.getAttribute("username").toString(),registerDto.getAssignee(),
                LocalDate.now(),LocalDate.parse(registerDto.getDueDate(), DateTimeFormatter.ISO_DATE),registerDto.getMilestone());

        TaskDto taskDto = taskService.registerTask(registerDto.getProjectName(),taskRegister);
        for(Long tag:registerDto.getTag()){
            taskTagService.registerTaskTag(registerDto.getProjectName(),taskDto.getTaskId(),tag);
        }
        return "redirect:/projects/"+registerDto.getProjectName()+"/task/"+taskDto.getTaskId();
    }
    @GetMapping("/project/{projectId}/task/{taskId}/delete")
    public String taskDelete(@PathVariable Long projectId,@PathVariable Long taskId){
        taskService.deleteTask(taskId);
        return "redirect:/project/"+projectId;
    }
    @GetMapping("/project/{projectId}/task/{taskId}/modify")
    public String taskModify(@PathVariable Long projectId,@PathVariable Long taskId,Model model){
        ProjectDto projectDto = null;
        try {
            projectDto = projectService.getProject(projectId);
        } catch (ProjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<ProjectMemberId> projectMemberIds = memberService.getMemberIdsByProjectId(projectId);
        List<AccountDto> projectMembers = new ArrayList<>();

        for(ProjectMemberId member:projectMemberIds){
            log.info(member.getAccountId());
            projectMembers.add(accountService.getAccount(member.getAccountId()));
        }
        List<TaskTagDto> tagIds = taskTagService.getTagIdByTaskId(taskId);
        List<TagDto> tags = new ArrayList<>();
        for(TaskTagDto tagId: tagIds){
            tags.add(tagService.getTag(tagId.getTagId()));
        }
        model.addAttribute("Task",taskService.getTask(taskId));
        model.addAttribute("Tag",tags);
        model.addAttribute("Tags",tagService.getTagsByProjectId(projectId));
        model.addAttribute("Milestones",mileService.getMilestonesByProjectId(projectId));
        model.addAttribute("Project",projectDto);
        model.addAttribute("allAccounts",projectMembers);

        return "taskcreate";
    }
    @PostMapping("project/task/modify")
    public String taskModify2(@ModelAttribute TaskModify taskModify, HttpSession session){
        //task 등록
        //task tag 연결
        TaskRegister taskRegister = new TaskRegister(taskModify.getTitle(),taskModify.getContent(),session.getAttribute("username").toString(),taskModify.getAssignee(),
                LocalDate.now(),LocalDate.parse(taskModify.getDueDate(), DateTimeFormatter.ISO_DATE),taskModify.getMilestone());

        taskService.updateTask(taskModify.getProjectId(),taskModify.getTaskId(),taskRegister);
        for(Long tag:taskModify.getTag()){
            taskTagService.registerTaskTag(taskModify.getProjectId(),taskModify.getTaskId(),tag);
        }
        return "redirect:/projects/"+taskModify.getProjectId()+"/task/"+taskModify.getTaskId();
    }
}
