package nth347.main;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("src/main/webapp/").getAbsolutePath());
        File additionWebInfClasses = new File("build/classes"); // "target/classes" if Maven, "build/classes" if Grable
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        // Authentication
        tomcat.addUser("user", "user");
        tomcat.addRole("user", "user");
        tomcat.addUser("admin", "admin");
        tomcat.addRole("admin", "admin");

        tomcat.start();
        tomcat.getServer().await();
    }
}
