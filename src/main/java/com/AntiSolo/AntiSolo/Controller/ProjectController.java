package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.HelperEntities.ProjectRequest;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    public ProjectService projectService;


    @Autowired
    private UserService userService;

    @Autowired
    public JwtHelper jwtHelper;



    @PostMapping("/test")
    public String saveProject(@RequestBody User user, @RequestHeader("Authorization") String token) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("came with user = "+user.getUserName());
        return "hii";
    }
    @PostMapping("/save")
    public String saveProject(@RequestBody ProjectRequest projectRequest,@RequestHeader("Authorization") String token){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUser = authentication.getName();
//        projectRequest.setAuthor(jwtHelper.getUsernameFromToken(projectRequest.getAuthor()));
//        if(!loggedInUser.equals(projectRequest.getAuthor()))return "Invalid Request";
        // Remove "Bearer " prefix if present
//        System.out.println("arrived in post");
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        projectRequest.setFilled(1);
        String username = jwtHelper.getUsernameFromToken(token);
        projectRequest.setAuthor(username);
        System.out.println("sending for project service");
        return projectService.save(projectRequest);
//        return "working on the API";
    }
    @GetMapping("/getProject/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable ObjectId projectId){
        System.out.println("fetching project with id = "+projectId);
        Optional<Project> project = projectService.getProjectById(projectId);
        if(project.isPresent())return new ResponseEntity<>(project.get(), HttpStatus.OK);
        else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    // Fetch random projects while excluding already fetched ones
    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/randomPaginated")
    public ResponseEntity<List<Project>> getRandomProjects(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) List<String> excludeIds) {
        System.out.println("random projects called");
        // Convert String ID list to ObjectId list
        List<ObjectId> excludedObjectIds = (excludeIds != null) ?
                excludeIds.stream().map(ObjectId::new).collect(Collectors.toList())
                : new ArrayList<>();

        List<Project> projects = projectService.getRandomProjects(excludedObjectIds, limit);
        return ResponseEntity.ok(projects);
    }

    // Fetch random projects while excluding already fetched ones with tags feature
    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/RandomPagesWithTags")
    public ResponseEntity<List<Project>> getRandomProjectsExcludingTags(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) List<String> excludeIds,
            @RequestParam(required = true) List<String> tags) {
        System.out.println("random project exclude tags called with tags = ");
        for(String s:tags)System.out.print(s+" ");
        System.out.println();
        for(String s:excludeIds)System.out.print(s+" ");
        System.out.println();
        // Convert String ID list to ObjectId list
        List<ObjectId> excludedObjectIds = (excludeIds != null) ?
                excludeIds.stream().map(ObjectId::new).collect(Collectors.toList())
                : new ArrayList<>();
//        List<ObjectId> chackTags = (tags != null) ?
//                tags.stream().map(ObjectId::new).collect(Collectors.toList())
//                : new ArrayList<>();


        List<Project> projects = projectService.getRandomFilteredProjects(excludedObjectIds,tags,limit);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/ApplyToggleInProject/{projectId}")
    public ResponseEntity<Boolean> ApplyToggle(@PathVariable ObjectId projectId,@RequestHeader("Authorization") String token){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        Optional<Project> project = projectService.getProjectById(projectId);
        if(project.isEmpty())return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        boolean result = projectService.ApplyToggle(username,project.get());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/AcceptApplicant")
    public ResponseEntity<Boolean> AcceptApplicant(@RequestHeader("Authorization") String token,@RequestParam ObjectId projectId,
                                                   @RequestParam String applicantUsername){
        System.out.println("accept applicant called");
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        Optional<User> user = userService.getById(username);
        Optional<User> applicant = userService.getById(applicantUsername);
        Optional<Project> project = projectService.getProjectById(projectId);
        System.out.println("empty checking project = "+project.isEmpty()+" user = "+user.isEmpty()+" applicant = "+applicant.isEmpty());
        if(project.isEmpty() || user.isEmpty() || applicant.isEmpty())return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        System.out.println("is author legit "+project.get().getAuthor().equals(username));
        if(!project.get().getAuthor().equals(username))return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);

        boolean hasApplied = false;
        for(ObjectId prId:applicant.get().getApplied()){
            if(prId.equals(projectId)){
                hasApplied = true;
                break;
            }
        }
        if(!hasApplied)return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

        boolean result = projectService.acceptApplicant(project.get(),user.get(),applicant.get());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/reportProject")
    public void ReportProject(@RequestBody WarningEntity warningEntity){
        projectService.fileReport(warningEntity);
    }

    @PostMapping("/editProject")
    public ResponseEntity<Boolean> editProject(@RequestHeader("Authorization") String token,@RequestBody Project editedProject){
        System.out.println("arrived in edit project "+" with id = "+editedProject.getId());
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        boolean ans = projectService.editProject(editedProject,username);
        if(ans)return new ResponseEntity<>(true,HttpStatus.OK);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<Boolean> deleteProject(@RequestHeader("Authorization") String token,@PathVariable String id){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        ObjectId proejctId = new ObjectId(id);
        Optional<Project> project = projectService.getProjectById(proejctId);
        if(project.isEmpty())return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        if(!project.get().getAuthor().equals(username))return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
        projectService.deleteProject(id);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
}
