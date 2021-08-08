
package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CinemaController {

    private final Cinema cinema = new Cinema();

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping(path = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkSeat(@RequestBody Seat seat) {
        if (seat.getRow() > 9 || seat.getColumn() > 9 || seat.getRow() < 1 || seat.getColumn() < 1)
            return new ResponseEntity<>(JSONErrors.outOfBounds(),
                    HttpStatus.BAD_REQUEST);

        Seat[] seats = cinema.getSeats();
        int arrPos = cinema.getArrPos(seat.getRow(), seat.getColumn());
        Seat seatToBook = seats[arrPos];

        if (seatToBook.isTaken())
            return new ResponseEntity<>(JSONErrors.alreadyPurchased(),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(seatToBook.bookSeat(), HttpStatus.OK);
    }

    @PostMapping(path = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> returnToken(@RequestBody Token token) {
        Optional<Seat> seat = cinema.findSeat(token);

        if (seat.isEmpty())
            return new ResponseEntity<>(JSONErrors.wrongToken(), HttpStatus.BAD_REQUEST);

        Seat s = seat.get();
        return new ResponseEntity<>(s.returnSeat(), HttpStatus.OK);
    }

    @PostMapping(path = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStats(@RequestParam(required = false) @RequestBody String password) {
        if (password == null || !password.equals("super_secret"))
            return new ResponseEntity<>(JSONErrors.wrongPassword(), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(cinema.getStats(), HttpStatus.OK);
    }

}