package controllers;

import github.GithubApi;
import play.mvc.Result;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;

/**
 * Created by zem on 02/11/15.
 */
public class ApplicationApi extends Controller{

    public F.Promise<Result> search(String searchTerms){
        return GithubApi.search(searchTerms).map(repositories -> {return ok(Json.toJson(repositories));});
    }

    public F.Promise<Result> contributors(String repos){
        return GithubApi.contributors(repos).map(users -> {return ok(Json.toJson(users));});
    }

    public F.Promise<Result> commits(String repos){
        return GithubApi.commits(repos).map(commits -> {return ok(Json.toJson(commits));});
    }

    public F.Promise<Result> contributions(String repos){
        return GithubApi.search(repos).map(contributions -> {return ok(Json.toJson(contributions));});
    }
}
