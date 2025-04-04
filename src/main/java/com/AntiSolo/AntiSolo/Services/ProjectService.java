package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.HelperEntities.ProjectRequest;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Repository.ProjectRepo;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            Map.entry("Web Development", "https://cdn.prod.website-files.com/6344c9cef89d6f2270a38908/673f2a3b44c1ed4901bb43bb_6386328bea96dffacc89946b_d1.webp"),
            Map.entry("Mobile App Development", "https://www.etatvasoft.com/blog/wp-content/uploads/2022/08/mobile-app-development-methodologies.jpg"),
            Map.entry("Frontend Development", "https://media.geeksforgeeks.org/wp-content/uploads/20240703165023/Frontend-Development-(1).webp"),
            Map.entry("Backend Development", "https://media.geeksforgeeks.org/wp-content/uploads/20240701150157/Backend-Development.webp"),
            Map.entry("Full Stack Development", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9kSSgd9JhbQNXDv9kpXbHp6hzl3OiQ9zdJg&s"),
            Map.entry("Machine Learning", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4sXvAhlZ68GDr_LA9ho4jhOz7-8Mzp7d-pQ&s"),
            Map.entry("Artificial Intelligence", "https://www.vecteezy.com/vector-art/13899429-machine-learning-icon-artificial-intelligence-smart-machine-logo-template-vector-illustration"),
            Map.entry("Data Science", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Cloud Computing", "https://www.dreamstime.com/stock-image-cloud-computing-text-cloud-image-image33947891"),
            Map.entry("Cybersecurity", "https://www.pexels.com/search/web%20development%20with%20text/"),
            Map.entry("Blockchain", "https://www.dreamstime.com/blockchain-text-abstract-background-connection-fiber-data-encrypted-metaverse-concept-image259671697"),
            Map.entry("Internet of Things (IoT)", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Game Development", "https://www.pexels.com/search/web%20development%20with%20text/"),
            Map.entry("Virtual Reality (VR) & Augmented Reality (AR)", "https://www.pexels.com/search/web%20development%20with%20text/"),
            Map.entry("Embedded Systems", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("DevOps & Automation", "https://www.pexels.com/search/web%20development%20with%20text/"),
            Map.entry("Big Data & Analytics", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Networking & Security", "https://www.pexels.com/search/web%20development%20with%20text/"),
            Map.entry("Software Engineering & Development", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Database Management", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Computer Vision", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Natural Language Processing (NLP)", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Autonomous Systems", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("HealthTech", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("FinTech", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("EdTech", "https://www.pexels.com/search/website%20development%20text/"),
            Map.entry("Robotics", "https://www.pexels.com/search/website%20development%20text/")
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

}
