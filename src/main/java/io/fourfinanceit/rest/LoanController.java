package io.fourfinanceit.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.fourfinanceit.validation.AmountValidator;
import io.fourfinanceit.validation.TermValidator;
import org.springframework.web.bind.annotation.*;

import io.fourfinanceit.dto.LoanDto;
import io.fourfinanceit.service.LoanService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/loans", method = POST)
public class LoanController {

    private final AmountValidator amountValidator;
    private final TermValidator termValidator;
    private final LoanService loanService;

    public LoanController(AmountValidator amountValidator, TermValidator termValidator, LoanService loanService) {
        this.amountValidator = amountValidator;
        this.termValidator = termValidator;
        this.loanService = loanService;
    }

    @RequestMapping(method = POST)
    public LoanDto create(@RequestParam("username") String username,
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam("term") long term,
                          HttpServletRequest request) {
        amountValidator.validate(amount);
        termValidator.validate(term);
        String ip = request.getRemoteAddr();
        return loanService.create(username, amount, term, ip);
    }

    @RequestMapping(value = "/extend/{id}", method = POST)
    public LoanDto extendTerm(@RequestParam("username") String username,
                              @PathVariable("id") long id,
                              @RequestParam("term") long term) {
        termValidator.validate(term);
        return loanService.extend(username, id, term);
    }

    @RequestMapping(value = "/{username}", method = GET)
    public List<LoanDto> getAll(@PathVariable("username") String username) {
        return loanService.getAll(username);
    }
}