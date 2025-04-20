package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.HelperEntities.ProjectRequest;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Repository.ProjectRepo;
import com.AntiSolo.AntiSolo.Repository.ReportRepo;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ProjectService {
    @Autowired
    public ProjectRepo projectRepo;

    @Autowired
    public UserService userService;

    @Autowired
    UserRepo ur;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ReportRepo reportRepo;

    public static final Set<String> projectDomains = Set.of(
            "Web Development", "Mobile App Development", "Frontend Development",
            "Backend Development", "Full Stack Development", "Machine Learning",
            "Artificial Intelligence", "Data Science", "Cloud Computing",
            "Cybersecurity", "Blockchain", "Internet of Things (IoT)",
            "Game Development", "Virtual Reality (VR) & Augmented Reality (AR)",
            "Embedded Systems", "DevOps & Automation", "Big Data & Analytics",
            "Networking & Security", "Software Engineering & Development",
            "Database Management", "Computer Vision", "Natural Language Processing (NLP)",
            "Autonomous Systems", "HealthTech", "FinTech", "EdTech", "Robotics"
    );
    public static final Map<String, String> projectDomainImages = Map.ofEntries(
            Map.entry("Web Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088295/673f2a3b44c1ed4901bb43bb_6386328bea96dffacc89946b_d1_tx1xwe.webp"),
            Map.entry("Mobile App Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088357/mobile-app-development-methodologies_twls9y.jpg"),
            Map.entry("Frontend Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088401/Frontend-Development-_1_qbykn6.webp"),
            Map.entry("Backend Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088450/Backend-Development_z0xors.webp"),
            Map.entry("Full Stack Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088489/images_gfgqs9.png"),
            Map.entry("Machine Learning", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088534/images_urdx5m.jpg"),
            Map.entry("Artificial Intelligence", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088681/artificial-technology-with-head-connection-line-logo-design_1042756-363_oxgcre.avif"),
            Map.entry("Data Science", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088768/istockphoto-1487248005-640x640_g87rqu.jpg"),
            Map.entry("Cloud Computing", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088846/images_1_bk5lky.jpg"),
            Map.entry("Cybersecurity", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745088965/istockphoto-1419462882-640x640_igq3b8.jpg"),
            Map.entry("Blockchain", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089034/istockphoto-1227588477-640x640_uab02n.jpg"),
            Map.entry("Internet of Things (IoT)", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089107/istockphoto-1184401187-612x612_vzyph7.jpg"),
            Map.entry("Game Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089186/png-transparent-video-game-development-computer-icons-video-game-developer-design-thumbnail_wb01rh.png"),
            Map.entry("Virtual Reality (VR) & Augmented Reality (AR)", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089287/p36new_hrpip9.jpg"),
            Map.entry("Embedded Systems", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089348/images_2_ccbyqg.jpg"),
            Map.entry("DevOps & Automation", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089400/istockphoto-1884505613-640x640_coxemq.jpg"),
            Map.entry("Big Data & Analytics", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089454/png-clipart-big-data-data-analysis-data-science-analytics-bigdata-text-orange-thumbnail_xteqpi.png"),
            Map.entry("Networking & Security", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089534/hp0f3siqf3_k3up3g.jpg"),
            Map.entry("Software Engineering & Development", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089614/software-engineering-chart-with-keywords-and-icons-sketch-G2NGCM_rtthsw.jpg"),
            Map.entry("Database Management", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089680/database-management-systems-dbms-software-600nw-2111606243_itg09q.webp"),
            Map.entry("Computer Vision", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089734/gettyimages-2155079682-640x640_nulnki.jpg"),
            Map.entry("Natural Language Processing (NLP)", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089786/cb3b2b7e5bfccd832005fb9537f76f132b241c3_foxota.jpg"),
            Map.entry("Autonomous Systems", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089856/gettyimages-1431036626-640x640_e5i0cb.jpg"),
            Map.entry("HealthTech", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089906/gettyimages-2160268764-640x640_f3mrm6.jpg"),
            Map.entry("FinTech", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745089976/fintech-financial-technology-online-banking-and-crowdfunding-vector_x2bgwv.jpg"),
            Map.entry("EdTech", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745090075/Ed-tech-Revolution_atx85e.jpg"),
            Map.entry("Robotics", "https://res.cloudinary.com/dx7bcuxjn/image/upload/v1745090160/768-512-16792401-431-16792401-1667206541553_zueshf.jpg")
    );


    public static final Set<String> technologyTags = Set.of(
            // Programming Languages
            "Java", "Python", "JavaScript", "C#", "C++", "Ruby", "Go", "Swift", "Kotlin", "PHP", "TypeScript", "Rust", "Dart", "Scala", "Perl", "Objective-C", "Haskell", "Lua", "MATLAB", "R",
            // Web Development
            "HTML", "CSS", "React", "Angular", "Vue.js", "Svelte", "Next.js", "Nuxt.js", "Gatsby", "jQuery", "Bootstrap", "Tailwind CSS", "Sass", "LESS", "Webpack", "Parcel", "Grunt", "Gulp",
            // Backend Frameworks
            "Spring Boot", "Django", "Flask", "Express.js", "NestJS", "Ruby on Rails", "Laravel", "ASP.NET Core", "Koa.js", "Gin", "Fiber", "Phoenix", "Play Framework",
            // Mobile Development
            "React Native", "Flutter", "SwiftUI", "Jetpack Compose", "Xamarin", "Ionic", "Cordova", "NativeScript",
            // Databases
            "MySQL", "PostgreSQL", "MongoDB", "SQLite", "MariaDB", "Oracle Database", "Microsoft SQL Server", "Firebase Realtime Database", "Cassandra", "Redis", "CouchDB", "DynamoDB", "Neo4j", "Elasticsearch",
            // DevOps & CI/CD
            "Docker", "Kubernetes", "Jenkins", "Travis CI", "CircleCI", "GitLab CI", "GitHub Actions", "Ansible", "Puppet", "Chef", "Terraform", "Vagrant", "Bamboo", "TeamCity",
            // Cloud Platforms
            "AWS", "Azure", "Google Cloud Platform (GCP)", "IBM Cloud", "Oracle Cloud", "DigitalOcean", "Heroku", "Netlify", "Vercel", "Firebase", "Linode",
            // Machine Learning & Data Science
            "TensorFlow", "Keras", "PyTorch", "Scikit-Learn", "Pandas", "NumPy", "Matplotlib", "Seaborn", "NLTK", "OpenCV", "XGBoost", "LightGBM", "Hugging Face Transformers", "Apache Spark", "Dask", "RapidMiner",
            // Testing Frameworks
            "JUnit", "Mockito", "Selenium", "Cypress", "Jest", "Mocha", "Chai", "RSpec", "PyTest", "Robot Framework", "TestNG", "Cucumber", "Appium",
            // Version Control
            "Git", "SVN", "Mercurial", "Perforce",
            // Project Management & Collaboration
            "JIRA", "Trello", "Asana", "Slack", "Confluence", "Notion", "Basecamp", "Monday.com", "ClickUp",
            // Security
            "OWASP", "Metasploit", "Nmap", "Wireshark", "Burp Suite", "Snort", "Nessus", "Kali Linux",
            // Miscellaneous
            "GraphQL", "REST", "gRPC", "SOAP", "WebSockets", "Microservices", "Serverless", "Progressive Web Apps (PWA)", "Single Page Applications (SPA)", "Jamstack", "Headless CMS", "Contentful", "Strapi", "WordPress", "Drupal", "Magento", "Shopify", "WooCommerce", "Salesforce", "SAP", "Oracle ERP", "Power BI", "Tableau", "QlikView", "Apache Kafka", "RabbitMQ", "ActiveMQ", "ZeroMQ", "Unity", "Unreal Engine", "Blender", "Figma", "Adobe XD", "Sketch", "InVision", "Zeplin", "Canva", "Simulink", "LabVIEW", "AutoCAD", "SolidWorks", "ANSYS", "MATHEMATICA", "SPSS", "SAS"
    );


    public static final Set<String> TECHNOLOGIES = new HashSet<>(Arrays.asList(
            // Programming Languages
            "Java", "Python", "JavaScript", "C#", "C++", "Ruby", "Go", "Swift", "Kotlin", "PHP", "TypeScript", "Rust", "Dart",
            // Front-End Frameworks and Libraries
            "React", "Angular", "Vue.js", "Svelte", "jQuery", "Bootstrap", "Tailwind CSS", "Foundation",
            // Back-End Frameworks and Libraries
            "Node.js", "Express.js", "Django", "Flask", "Ruby on Rails", "Spring Boot", "ASP.NET Core", "Laravel", "Symfony",
            // Mobile Development
            "React Native", "Flutter", "SwiftUI", "Kotlin Multiplatform Mobile", "Xamarin", "Ionic",
            // Databases
            "MySQL", "PostgreSQL", "MongoDB", "SQLite", "Firebase", "Oracle Database", "Microsoft SQL Server", "Redis", "Cassandra",
            // DevOps and CI/CD Tools
            "Docker", "Kubernetes", "Jenkins", "Travis CI", "CircleCI", "GitHub Actions", "GitLab CI", "Terraform", "Ansible", "Puppet",
            // Cloud Platforms
            "Amazon Web Services (AWS)", "Microsoft Azure", "Google Cloud Platform (GCP)", "IBM Cloud", "Oracle Cloud", "DigitalOcean", "Heroku", "Netlify", "Vercel",
            // Machine Learning and Data Science
            "TensorFlow", "PyTorch", "Keras", "Scikit-Learn", "Pandas", "NumPy", "Matplotlib", "Seaborn", "NLTK", "OpenCV",
            // Testing Frameworks
            "JUnit", "Selenium", "Cypress", "Mocha", "Jest", "RSpec",
            // Package Managers
            "npm", "Yarn", "pip", "Composer", "NuGet",
            // Version Control
            "Git", "Subversion (SVN)", "Mercurial",
            // APIs and Protocols
            "GraphQL", "REST", "gRPC", "SOAP",
            // Content Management Systems (CMS)
            "WordPress", "Drupal", "Joomla", "Magento", "Shopify",
            // Miscellaneous
            "Webpack", "Babel", "ESLint", "Prettier", "Gulp", "Grunt", "Three.js", "D3.js"
    ));

    public void saveDirect(Project project){
        projectRepo.save(project);
    }

    public String save(ProjectRequest projectRequest){
        Optional<User> user = userService.getById(projectRequest.getAuthor());
        if(!user.isPresent())return "Invalid Request!";
        if(projectRequest.getTeamSize()<=1)return "Team size can not be 0 or 1";
        if(projectRequest.getDomain()==null || projectRequest.getDomain().isEmpty())return "Select Valid Domain!";
        if(!projectDomains.contains(projectRequest.getDomain()))return "Invalid domain name!";
        if(projectRequest.getTitle().trim().isEmpty())return "title can not be empty";
        projectRequest.setTeamSize(Math.max(projectRequest.getTeamSize(),2));
        HashSet<String> tags = new HashSet<>();
        for(String i:projectRequest.getTags()){
            if(!technologyTags.contains(i))return "Invalid tag value";
            tags.add(i);
        }
        for(String s:projectRequest.getTechnologies()){
            if(!TECHNOLOGIES.contains(s))return "Technology "+s+" does not exists!";
        }
        List<Member> members = new ArrayList<>();
        members.add(new Member(user.get().getUserName(),user.get().getProfileImageUrl()));
//        members.add()
        //settting project image from hashmap
        projectRequest.setImage(projectDomainImages.get(projectRequest.getDomain()));
        Project p = new Project(user.get().getUserName(),projectRequest.getTeamSize(),projectRequest.getFilled(),projectRequest.getDomain(),tags,
                projectRequest.getImage(),projectRequest.getTitle(),projectRequest.getDescription(),projectRequest.getTechnologies(),members);
        p.setAuthorImage(user.get().getProfileImageUrl());
        System.out.println("could not save project p = "+p);
        projectRepo.save(p);
        user.get().addproject(p.getId());
        user.get().addTeamProject(p.getId());
        ur.save(user.get());

                        //after saving the user also reload the entire page so that it again fetches the
        //user details and the array with this project added is also added to list
        return "Saved successfully";
    }
    public Optional<Project> getProjectById(ObjectId projectid){
        return projectRepo.findById(projectid);
    }

    public List<Project> getRandomProjects(List<ObjectId> excludedIds, int limit) {
        return projectRepo.findRandomProjectsExcluding(excludedIds, limit);
    }




    public boolean ApplyToggle(String username,Project project){
        Optional<User> user = ur.findById(username);
        if(user.isEmpty())return false;
        ObjectId projectId = project.getId();
        for(ObjectId id:user.get().getTeams()){
            if(id.equals(projectId))return false;
        }
        Iterator<ObjectId> iterator = user.get().getApplied().iterator();
        boolean applied = false;
        while (iterator.hasNext()) {
            if (iterator.next().equals(projectId)) {
                iterator.remove();
                applied = true;
                break;
            }
        }
        if(applied){

            userService.saveDirect(user.get());
            // Safely removes the element
            project.getApplicants().removeIf(member -> member.getName().equals(username));
            saveDirect(project);
            return true;
        }
        user.get().addAppliedProject(projectId);
        userService.saveDirect(user.get());
        project.addApplicant(new Member(user.get().getUserName(),user.get().getProfileImageUrl()));
        saveDirect(project);
        notificationService.newApplicationforProjectNotification(user.get().getUserName(),project.getAuthor(),project.getTitle(),projectId);
        return true;
    }


    public boolean acceptApplicant(Project project,User author,User applicant){
        //I have extracted author data as well because we will have to push notification to author id in fiture
        // Safely removes the element
        project.getApplicants().removeIf(member -> member.getName().equals(applicant.getUserName()));
        project.addMember(new Member(applicant.getUserName(),applicant.getProfileImageUrl()));
        applicant.getApplied().removeIf(application->application.equals(project.getId()));
        applicant.addTeamProject(project.getId());
        project.setFilled(project.getFilled()+1);
        project.setTeamSize(Math.max(project.getTeamSize(),project.getFilled()+1));
        saveDirect(project);
        userService.saveDirect(applicant);
        notificationService.newApplicationforProjectNotification(applicant.getUserName(),project.getAuthor(),project.getTitle(),project.getId());
        return true;
    }

    // Fetch random projects excluding some IDs and filtering by tags
    public List<Project> getRandomFilteredProjects(List<ObjectId> excludedIds, List<String> tags, int limit) {
        return projectRepo.findRandomProjectsExcludingWithTags(excludedIds, tags, limit);
    }

    public boolean checkMember(User user,String projectId){
        ObjectId obj = new ObjectId(projectId);
        for(ObjectId prj:user.getTeams()){
//            System.out.println("prj = "+prj+" obj = "+obj);
            if(prj.equals(obj))return true;
        }
        return false;
//        Optional<Project> project = projectRepo.findById(new ObjectId(projectId));
//        if(project.isEmpty())return false;
//        for(Member member:project.get().getMembers()){
//            if(member.getName().equals(user.getUserName()))return true;
//        }
//        return false;
    }

    public void fileReport(WarningEntity warningEntity){
        String projectId = warningEntity.getProjectId();

        Optional<Project> p = getProjectById(new ObjectId(projectId));
        if(p.isEmpty())throw new RuntimeException();;
        Optional<User> user = userService.getById(p.get().getAuthor());
        if(user.isEmpty())throw new RuntimeException();;
        Optional<ReportEntity> temp = reportRepo.findByProjectId(projectId);
        if(temp.isPresent())throw new RuntimeException();
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setProjectId(projectId);
        reportEntity.setEmail(user.get().getEmail());
        reportEntity.setType(0);
        reportEntity.setUserName(user.get().getUserName());
        reportEntity.setCreatedAt(Instant.now());
        reportEntity.setMessage(warningEntity.getMessage());
        reportEntity.setReportFrom(warningEntity.getReportFrom());
        reportRepo.save(reportEntity);
    }

    public boolean editProject(Project editedProject,String sender){
        System.out.println("...0");
        System.out.println(editedProject);
        Optional<Project> project = getProjectById(editedProject.getId());
        if(!project.get().getAuthor().equals(sender))return false;
//        Optional<User> user = userService.getById(editedProject.getAuthor());
//        if(user.isEmpty()|| project.isEmpty() || !project.get().getAuthor().equals(sender))return false;
        System.out.println("...1");
        System.out.println("...2");
        if(editedProject.getTeamSize()<=1)return false;
//        if(editedProject.getDomain()==null || editedProject.getDomain().isEmpty())return false;
//        if(!projectDomains.contains(editedProject.getDomain()))return false;
        editedProject.setTeamSize(Math.max(editedProject.getTeamSize(),2));
//        HashSet<String> tags = new HashSet<>();
        System.out.println("...3");
        for(String i:editedProject.getTags()){
            if(!technologyTags.contains(i))return false;
//            tags.add(i);
        }
        System.out.println("...4");
        for(String s:editedProject.getTechnologies()){
            if(!TECHNOLOGIES.contains(s))return false;
        }

        System.out.println("...5");
        project.get().setStatus(editedProject.getStatus());
        project.get().setTeamSize(editedProject.getTeamSize());
        project.get().setDescription(editedProject.getDescription());
        project.get().setTechnologies(editedProject.getTechnologies());
        project.get().setTags(editedProject.getTags());

        //update members' info too that they have been kicked out of the project!
        for(Member member:project.get().getMembers()){
            boolean removed = true;
            for(Member m:editedProject.getMembers()){
                if(m.getName().equals(member.getName()))removed = false;
            }
            if(removed){
                Optional<User> toBeRemovedMember = userService.getById(member.getName());
                if(toBeRemovedMember.isEmpty())continue;
                toBeRemovedMember.get().getTeams().removeIf(id->id.equals(project.get().getId()));
                userService.saveDirect(toBeRemovedMember.get());
            }
        }
        //members ko update kar do jo accept ho gaye hai
        for(Member member:editedProject.getMembers()){
            boolean accept = true;
            for(Member m:project.get().getMembers()){
                if(m.getName().equals(member.getName()))accept = false;
            }


            Optional<User> author = userService.getById(project.get().getAuthor());
            Optional<User> applicant = userService.getById(member.getName());
            if(accept)acceptApplicant(project.get(),author.get(),applicant.get());
        }
        project.get().setMembers(editedProject.getMembers());
        //un applicants ko bhi bata de ki vo reject ho rhe hain ya accept ho gaye so they ae no longer in applicants list
        for(Member applicant:project.get().getApplicants()){
            boolean removed = true;
            for(Member m:editedProject.getApplicants()){
                if(m.getName().equals(applicant.getName()))removed = false;
            }
            for(Member m:editedProject.getMembers()){
                if(m.getName().equals(applicant.getName()))removed = true;
            }
            if(removed){
                Optional<User> toBeRemovedMember = userService.getById(applicant.getName());
                if(toBeRemovedMember.isEmpty())continue;
//                System.out.println(" before removing "+project.get().getId()+" applicant = "+toBeRemovedMember.get().getUserName());
//                for(ObjectId d:toBeRemovedMember.get().getProjects()){
//                    System.out.println(d);
//                }

                toBeRemovedMember.get().getApplied().removeIf(id->id.equals(project.get().getId()));

//                System.out.println("after removeing ");
//                for(ObjectId d:toBeRemovedMember.get().getProjects()){
//                    System.out.println(d);
//                }
                userService.saveDirect(toBeRemovedMember.get());
            }
        }
        project.get().setApplicants(editedProject.getApplicants());
        project.get().setFilled(project.get().getMembers().size());
        project.get().getApplicants().removeIf(elem->project.get().getMembers().contains(elem));
        saveDirect(project.get());
        return true;
    }

    public void removeApplication(String userName,ObjectId projectId){
        Optional<User> user = userService.getById(userName);
        if(user.isEmpty())return;
        user.get().getApplied().removeIf(id -> id.equals(projectId));
        userService.saveDirect(user.get());
    }


    public void removeMember(String userName,ObjectId projectId){
        Optional<User> user = userService.getById(userName);
        if(user.isEmpty())return;
        user.get().getTeams().removeIf(id -> id.equals(projectId));
        user.get().getProjects().removeIf(id -> id.equals(projectId));
        userService.saveDirect(user.get());
    }


    public void deleteProject(String id){
        ObjectId projectId = new ObjectId(id);
        Optional<Project> project = getProjectById(projectId);
        if(project.isEmpty())return;
        for(Member applicant:project.get().getApplicants()){
            removeApplication(applicant.getName(),projectId);
        }
        for(Member member:project.get().getMembers()){
            removeMember(member.getName(),projectId);
        }
        projectRepo.deleteById(projectId);


    }

}
