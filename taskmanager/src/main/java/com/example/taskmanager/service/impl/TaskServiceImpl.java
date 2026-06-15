package com.example.taskmanager.service.impl;


import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.enums.Category;
import com.example.taskmanager.exceptions.BadRequestException;
import com.example.taskmanager.exceptions.NotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Override
    public ApiResponse<TaskDTO> createTask(TaskDTO taskRequest, String userEmail) {

        log.info("Description is: {}", taskRequest.getDescription());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCategory(taskRequest.getCategory() != null ? taskRequest.getCategory() : Category.PERSONAL);
        task.setUser(user);

        Task savedTasks = taskRepository.save(task);
        TaskDTO savedTaskDto = mapTaskToTaskDTO(savedTasks);

        return new ApiResponse<>(201, "Tasks created successfully", savedTaskDto);


    }

    @Override
    public ApiResponse<List<TaskDTO>> getTasksByUserAndCategory(String email, Category category) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        List<Task> tasks;
        if (category != null) {
            tasks = taskRepository.findByUserAndCategory(user, category);
        } else {
            tasks = taskRepository.findByUser(user);
        }

        List<TaskDTO> taskDTOS = tasks.stream()
                .map(this::mapTaskToTaskDTO)
                .toList();

        String message = (category != null) ? "User Tasks category: " + category : "All your tasks retrieved";

        return new ApiResponse<>(200, message, taskDTOS);
    }


    @Override
    public ApiResponse<String> deleteTask(Long id, String email) {


        if (!taskRepository.existsById(id)) throw new NotFoundException("Tasks not found");

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));

        if (!task.getUser().getEmail().equals(email)) {
            throw new BadRequestException("You are not authorized to Delete this task. it doesn't belong to you");
        }

        taskRepository.deleteById(id);

        return new ApiResponse<>(204, "task deleted successfully", null);
    }


    @Override
    public ApiResponse<TaskDTO> toggleTaskCompletion(Long id, String email) {


        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));

        if (!task.getUser().getEmail().equals(email)) {
            throw new BadRequestException("You are not authorized to update this task. it doesn't belong to you");
        }


        task.setCompleted(!task.isCompleted());

        Task updatedTask = taskRepository.save(task);
        String status = updatedTask.isCompleted() ? "Completed" : "Pending";

        return new ApiResponse<>(200, "Task marked as " + status, mapTaskToTaskDTO(updatedTask));

    }


    private TaskDTO mapTaskToTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCategory(task.getCategory());
        taskDTO.setCompleted(task.isCompleted());
        return taskDTO;
    }
}