package ru.imakabr.votingsystemboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.imakabr.votingsystemboot.model.Restaurant;
import ru.imakabr.votingsystemboot.model.User;
import ru.imakabr.votingsystemboot.model.Vote;
import ru.imakabr.votingsystemboot.repository.UserRepository;
import ru.imakabr.votingsystemboot.repository.VoteRepository;


import java.time.LocalDate;
import java.util.List;

import static ru.imakabr.votingsystemboot.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public Vote create(Restaurant restaurant, int userId) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkTime();
        Vote vote = new Vote(null, userRepository.getOne(userId), restaurant, LocalDate.now());
        return voteRepository.save(vote);
    }

    public void update(Restaurant restaurant, int userId, int voteId) {
        Assert.notNull(restaurant, "restaurant must not be null");
        LocalDate dateVote = get(voteId, userId).getDate();
        checkDateTime(dateVote);
        Vote vote = new Vote(voteId, userRepository.getOne(userId), restaurant, dateVote);
        checkNotFoundWithId(voteRepository.save(vote), voteId);
    }

    public void delete(int voteId, int userId) {
        Vote vote = get(voteId, userId);
        checkDateTime(vote.getDate());
        checkNotFoundWithId(voteRepository.delete(voteId, userId) != 0, voteId);
    }

    public Vote get(int voteId, int userId) {
        return checkNotFoundWithId(voteRepository.findById(voteId).filter(vote -> vote.getUser().getId() == userId).orElse(null), voteId);
    }

    public List<Vote> getAllVotesByUserId(int userId) {
        return checkNotFoundWithId(voteRepository.getAllVotesByUserId(userId), userId);
    }

    public List<Vote> getAllVotesByRestaurantId(int restId) {
        return checkNotFoundWithId(voteRepository.getAllVotesByRestaurantId(restId), restId);
    }

    public Vote getVoteByUserIdAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getVoteByUserIdAndDate(userId, date), userId);
    }

    public List<User> getAllUsersByRestaurantIdAndDate(int restId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getAllUsersByRestaurantIdAndDate(restId, date), restId);
    }

}
