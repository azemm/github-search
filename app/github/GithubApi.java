package github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Commit;
import model.Repository;
import model.User;
import model.UserContribution;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zem on 28/10/15.
 */
public class GithubApi {

    private final static String URL = "https://api.github.com";
    private final static String URL_REPOS = URL + "/repos";
    private final static String SEARCH = "search/repositories";
    private final static String CONTRIBUTORS = "contributors";
    private final static String COMMITS = "commits";
    private final static String TOKEN = "35d3c991e7c4ef8ed66161675f5f613a806fbf8d";

    private static WSRequest request(String url){
        return WS.url(url);
	//.setQueryParameter("access_token", TOKEN);
    }


    public static F.Promise<List<Repository>> search(String keyword){
        return request(URL + "/" + SEARCH).setQueryParameter("q", keyword).get().map(wsResponse -> {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = wsResponse.asJson();
            return mapper.readValue(json.get("items").toString(), new TypeReference<List<Repository>>() {
            });
        });
    }

    public static F.Promise<List<User>> contributors(String fullName){
        return request(URL_REPOS + "/" + fullName + "/" + CONTRIBUTORS).get().map(wsResponse -> {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = wsResponse.asJson();
            return mapper.readValue(json.toString(), new TypeReference<List<User>>(){});
        });
    }

    public static F.Promise<List<Commit>> commits(String fullName){
        return request(URL_REPOS + "/" + fullName + "/" + COMMITS).get().map(wsResponse -> {
            List<Commit> commits = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = wsResponse.asJson();
            json.forEach(jsonNode -> {
                try {
                    Commit commit = new Commit();
                    commit.setSha(jsonNode.get("sha").asText());
                    commit.setDate(jsonNode.get("commit").get("committer").get("date").asText());
                    commit.setHtml_url(jsonNode.get("html_url").asText());
                    User committer = mapper.readValue(jsonNode.get("committer").toString(), User.class);

                    if(committer == null){
                        committer = new User();
                        committer.setLogin("");
                    }
                    commit.setCommitter(committer);
                    commits.add(commit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return commits.subList(0, Integer.min(100, commits.size()));
        });
    }

    public static F.Promise<List<UserContribution>> contributions(String fullName){
        return commits(fullName).map(commits -> contributions(commits));
    }

    public static List<UserContribution> contributions(List<Commit> commits){
        List<UserContribution> contributions = new ArrayList<>();
        Map<User, Long> map = commits.stream().map(commit -> commit.getCommitter()).filter(user1 -> user1 != null).collect(Collectors.groupingBy(user -> user, Collectors.counting()));

        map.forEach((user, n) -> {
            contributions.add(new UserContribution(user, n));
        });

        contributions.sort((o1, o2) -> (int)(o2.getCommits() - o1.getCommits()));
        return contributions;
    }
}
