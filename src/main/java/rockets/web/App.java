package rockets.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.resource.ClassPathResource;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.logging.log4j.core.util.Closer.closeSilently;
import static spark.Spark.*;

//add comments
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    private static DAO dao;

    public static void setDao(DAO dao) {
        App.dao = dao;
    }

    public static void main(String[] args) throws IOException {
        Properties properties = loadProperties();

        int port = Integer.parseInt(properties.getProperty("spark.port"));
        port(port);

        String dbAddress = properties.getProperty("neo4j.dir");
        if (null == dao) {
            dao = new Neo4jDAO(dbAddress);
        }

        // "/"
        handleGetIndex();

        // "/hello"
        handleGetHello();

        // "/register"
        handleGetRegister();

        // "/register"
        handlePostRegister();

        // "/login"
        handleGetLogin();

        // "/login"
        handlePostLogin();

        // "/logout"
        handleGetLogout();

        // "/user/:id"
        handleGetUserById();

        // "/users"
        handleGetUsers();

        // "/rocket/create"
        handleGetCreateRocket();

        // "/rocket/create"
        handlePostCreateRocket();

        // "/rocket/:id"
        handleGetRocket();

        // "/rockets"
        handleGetRockets();

        // "/launches"
//        handleGetLaunches();

        // "/lsp/create"
        handleGetCreateLSP();

        // "/lsp/create"
        handlePostCreateLSP();

        // "/lsp/:id"
        handleGetLSP();

        // "/lsps"
        handleGetLSPs();

    }

    public static void stop() {
        Spark.stop();
    }

    private static void handleGetUsers() {
        get("/users", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                attributes.put("users", dao.loadAll(User.class));
                return new ModelAndView(attributes, "users.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "users.html.ftl");
            }
        }, new FreeMarkerEngine());

    }

    private static void handleGetIndex() {
        get("/", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            attributes.put("user", user);
            return new ModelAndView(attributes, "base_page.html.ftl");
            //return handleBaseHelloView(req, res, attributes);
        }, new FreeMarkerEngine());
    }

    private static void handleGetRegister() {
        get("/register", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", "");
            attributes.put("firstName", "");
            attributes.put("lastName", "");

            return new ModelAndView(attributes, "register.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handlePostRegister() {
        post("/register", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String email = req.queryParams("email");
            String password = req.queryParams("password");
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");

            attributes.put("email", email);
            //add password
            attributes.put("password", password);
            attributes.put("firstName", firstName);
            attributes.put("lastName", lastName);
            if (dao.getUserByEmail(email) != null)
                return handleException(res, attributes, new IllegalArgumentException("email is already exist"),
                        "register.html.ftl");

            logger.info("Registering <" + email + ">, " + password);

            User user;
            try {
                user = new User(firstName, lastName, password, email);
                dao.createOrUpdate(user);

                res.status(301);
                req.session(true);
                req.session().attribute("user", user);
                res.redirect("/hello");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "register.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetHello() {
        get("/hello", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            if (null != user) {
                attributes.put("user", user);
            }
            return new ModelAndView(attributes, "base_page.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handleGetLogin() {
        get("/login", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String user_name = req.params("user_name");
            if (null == user_name || user_name.trim().isEmpty()) {
                attributes.put("user_name", "");
            } else {
                attributes.put("user_name", user_name);
            }

            return new ModelAndView(attributes, "login.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handlePostLogin() {
        post("/login", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String user_name = req.queryParams("user_name");
            String password = req.queryParams("password");

            logger.info("Logging in <" + user_name + ">, " + password);

            User user = null;
            try {
                user = dao.getUserByEmail(user_name);
            } catch (Exception e) {
                handleException(res, attributes, e, "login.html.ftl");
            }
            if (null != user && user.getPassword().equals(password)) {
                res.status(301);
                req.session(true);
                req.session().attribute("user", user);
                res.redirect("/hello");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } else {
                attributes.put("errorMsg", "Invalid email/password combination.");
                attributes.put("user_name", user_name);
                return new ModelAndView(attributes, "login.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetLogout() {
        get("/logout", (req, res) -> {
            User user = getLoggedInUser(req);
            spark.Session session = req.session();
            if (null != session && null != user) {
                session.invalidate();
            }
            res.redirect("/");
            return "";
        });
    }

    private static ModelAndView handleException(Response res, Map<String, Object> attributes, Exception e,
                                                String templateName) {
        res.status(500);
        if (e instanceof SQLException && null != e.getCause()) {
            attributes.put("errorMsg", e.getCause().getMessage());
        } else {
            attributes.put("errorMsg", e.getMessage());
        }
        e.printStackTrace();
        return new ModelAndView(attributes, templateName);
    }


    private static User getLoggedInUser(Request req) {
        spark.Session session = req.session();
        User user = null;
        if (null != session) {
            user = session.attribute("user");
        }
        return user;
    }

    private static void handleGetUserById() {
        get("/user/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            attributes.put("user", user);
            try {
                String id = req.params(":id");
                User person = dao.load(User.class, Long.parseLong(id));
                if (null != person) {
                    attributes.put("user", person);
                } else {
                    attributes.put("errorMsg", "No user with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "user.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "user.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    // TODO: Need to TDD this
    private static void handleGetRocket() {
        get("/rocket/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                String id = req.params(":id");
                Rocket rocket = dao.load(Rocket.class, Long.parseLong(id));
                if (null != rocket) {
                    attributes.put("rocket", rocket);
                } else {
                    attributes.put("errorMsg", "No rocket with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "rocket.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "rocket.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    // TODO: Need to TDD this
    private static void handlePostCreateRocket() {
        post("/rocket/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String rocketName = req.queryParams("rocketName");
            String country = req.queryParams("country");
            String manufacturer = req.queryParams("manufacturer");

            attributes.put("rocketName", rocketName);
            attributes.put("country", country);
            attributes.put("manufacturer", manufacturer);

            logger.info("Creating Rocket <" + rocketName + ">");

            Rocket rocket;
            try {
                rocket = new Rocket();
                rocket.setName(rocketName);
                rocket.setCountry(country);
                LaunchServiceProvider lsp = dao.getLSPByName(manufacturer);
                rocket.setManufacturer(lsp);
                dao.createOrUpdate(rocket);

                res.status(301);
                req.session(true);
                req.session().attribute("rocket", rocket);
                res.redirect("/register");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "create_rocket.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    // TODO: Need to TDD this
    private static void handleGetCreateRocket() {
        get("/rocket/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("rocketName", "");
            attributes.put("country", "");
            attributes.put("manufacturer", "");

            return new ModelAndView(attributes, "create_rocket.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handleGetRockets() {
        get("/rockets", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                attributes.put("rockets", dao.loadAll(Rocket.class));
                return new ModelAndView(attributes, "rockets.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "rockets.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static Properties loadProperties() throws IOException {
        ClassPathResource resource = new ClassPathResource("app.properties");
        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            properties.load(stream);
            return properties;
        } finally {
            closeSilently(stream);
        }
    }

    private static void handlePostCreateLSP() {
        post("/lsp/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String lspName = req.queryParams("lspName");
            String yearFounded = req.queryParams("yearFounded");
            String country = req.queryParams("country");

            attributes.put("lspName", lspName);
            //add password
            attributes.put("yearFounded", yearFounded);
            attributes.put("country", country);

            logger.info("Creating LSP <" + lspName + ">");

            LaunchServiceProvider lsp;
            try {
                lsp = new LaunchServiceProvider();
                lsp.setName(lspName);
                lsp.setYearFounded(yearFounded);
                lsp.setCountry(country);
                if (dao.getLSPByName(lspName) != null)
                    throw new IllegalArgumentException("lsp is already exist");

                dao.createOrUpdate(lsp);

                res.status(301);
                req.session(true);
                req.session().attribute("lsp", lsp);
                res.redirect("/lsp/create");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "create_lsp.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetCreateLSP() {
        get("/lsp/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("lspName", "");
            attributes.put("yearFounded", "");
            attributes.put("country", "");

            return new ModelAndView(attributes, "create_lsp.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handleGetLSP() {
        get("/lsp/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                String id = req.params(":id");
                LaunchServiceProvider lsp = dao.load(LaunchServiceProvider.class, Long.parseLong(id));
                if (null != lsp) {
                    attributes.put("lsp", lsp);
                } else {
                    attributes.put("errorMsg", "No launch service provider with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "lsp.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "lsp.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetLSPs() {
        get("/lsps", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                attributes.put("lsps", dao.loadAll(LaunchServiceProvider.class));
                return new ModelAndView(attributes, "lsps.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "lsps.html.ftl");
            }
        }, new FreeMarkerEngine());
    }
}
