package controllers;

import github.GithubApi;
import model.UserContribution;
import play.mvc.Result;
import play.libs.F;
import play.mvc.Controller;

import java.util.List;

public class Application extends Controller {

    public Result index(){
        return ok(views.html.index.render());
    }

    public F.Promise<Result> search(String searchTerms){
        return GithubApi.search(searchTerms).map(repositories -> {
            return ok(views.html.searchResults.render(searchTerms, repositories));
        });
    }

    public F.Promise<Result> details(String repos){
        return GithubApi.contributors(repos).flatMap(users -> {
            return GithubApi.commits(repos).map(commits -> {
                List<UserContribution> userContributions = GithubApi.contributions(commits);
                return ok(views.html.details.render(repos, users, userContributions, commits));
            });
        });
    }
}
