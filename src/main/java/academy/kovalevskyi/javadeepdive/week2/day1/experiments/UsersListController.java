package academy.kovalevskyi.javadeepdive.week2.day1.experiments;

import academy.kovalevskyi.javadeepdive.week2.day0.JsonHelper;
import academy.kovalevskyi.javadeepdive.week2.day1.Controller;
import academy.kovalevskyi.javadeepdive.week2.day1.Get;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import academy.kovalevskyi.javadeepdive.week2.day1.Post;
import academy.kovalevskyi.javadeepdive.week2.day1.Put;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UsersListController {

  private List<User> users = new ArrayList<>();

  @Get
  @Path("/users")
  public String[] users() {
    return users.stream()
        .map(JsonHelper::toJsonString)
        .toArray(String[]::new);
  }

  @Post
  @Path("/users")
  public void addUser(User user) {
    users.add(user);
  }

  @Put
  @Path("/users")
  public void put(User user) {
    var existingUser = users.indexOf(user);
    if (existingUser == -1) {
      users.add(user);
    } else {
      // TODO something with existing user.
    }
  }
}
