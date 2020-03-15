package si.fri.mag;

import si.fri.mag.controllers.RootController;
import si.fri.mag.controllers.v1.AWSStorageController;
import si.fri.mag.mappers.ForbiddenExceptionMapper;
import si.fri.mag.mappers.NotFoundExceptionMapper;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class AWSStorageApi extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(AWSStorageController.class);
        resources.add(RootController.class);
        resources.add(ForbiddenExceptionMapper.class);
        resources.add(NotFoundExceptionMapper.class);
        return resources;
    }
}
