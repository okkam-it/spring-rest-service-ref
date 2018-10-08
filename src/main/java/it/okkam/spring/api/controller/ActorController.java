package it.okkam.spring.api.controller;

import it.okkam.spring.api.exceptions.ActorNotFoundException;
import it.okkam.spring.api.model.Actor;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = ActorController.BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ActorController {

  protected static final String BASE_URL = "/v1/actors";

  @GetMapping("/ping")
  public String ping() {
    return "Ping success! ";
  }

  @GetMapping("/error")
  public String errro() throws ActorNotFoundException {
    throw new ActorNotFoundException("app error");
  }

  /**
   * Creates an actor.
   * 
   * @param req
   *          the HttpServletRequest
   * @param name
   *          the actor name
   * @return the created actor
   */
  @PostMapping("/")
  public ResponseEntity<Object> createActor(HttpServletRequest req,
      @RequestParam("name") String name) {
    final Actor actor = new Actor(1L, name);
    return ResponseEntity.created(getActorUri(req, actor.getId())).body(actor);

  }

  private URI getActorUri(HttpServletRequest req, Long actorId) {
    return ServletUriComponentsBuilder.fromRequest(req)//
        .path(BASE_URL + "/" + actorId.toString())//
        .build()//
        .toUri();
  }
}
