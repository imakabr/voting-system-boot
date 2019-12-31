package ru.imakabr.votingsystemboot.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.imakabr.votingsystemboot.model.Restaurant;
import ru.imakabr.votingsystemboot.model.Vote;
import ru.imakabr.votingsystemboot.service.VoteService;
import ru.imakabr.votingsystemboot.web.SecurityUtil;


import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    public static final String REST_URL = "/rest/restaurants/votes";
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected VoteService voteService;

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll");
        return voteService.getAllVotesByUserId(SecurityUtil.authUserId());
    }

    @GetMapping("/today")
    public Vote getVoteToday() {
        LocalDate date = LocalDate.now();
        return voteService.getVoteByUserIdAndDate(SecurityUtil.authUserId(), date);
    }

    @GetMapping("/filter")
    public Vote getVoteByDate(@RequestParam(required = false) LocalDate date) {
        return voteService.getVoteByUserIdAndDate(SecurityUtil.authUserId(), date);
    }

    @PostMapping()
    public ResponseEntity<Vote> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create vote");
        Vote created = voteService.create(restaurant, SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/votes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update vote");
        voteService.update(restaurant, SecurityUtil.authUserId(), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote");
        voteService.delete(id, SecurityUtil.authUserId());
    }
}
