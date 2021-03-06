package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.exception.AccountNotFoundException;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.AccountPostDto;
import io.hexlet.hexletcorrection.dto.mapper.AccountMapper;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;

@RestController
@RequestMapping(API_PATH_V1 + ACCOUNTS_PATH)
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable("id") Long id) {
        return accountMapper.toAccountDto(
                accountService
                        .findById(id)
                        .orElseThrow(() -> new AccountNotFoundException(id))
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@Valid @RequestBody AccountPostDto account) {
        return accountMapper.toAccountDto(
                accountService.create(
                        accountMapper.postDtoToAccount(account)
                )
        );
    }

    @GetMapping
    public List<AccountDto> getAccounts(@RequestParam(required = false) String name) {
        if (name == null) {
            return accountService.findAll()
                    .stream()
                    .map(accountMapper::toAccountDto)
                    .collect(Collectors.toList());
        }
        return accountService.findByName(name).stream()
                .map(accountMapper::toAccountDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable("id") Long id) {
        accountService.delete(id);
    }
}
