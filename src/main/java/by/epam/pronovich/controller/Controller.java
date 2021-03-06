package by.epam.pronovich.controller;

import by.epam.pronovich.controller.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Controller {

    @Getter
    private final static Controller INSTANCE = new Controller();

    private final CommandProvider commandProvider = new CommandProvider();

    public void doAction(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) throws ServletException, IOException {
        String commandName = getCommandName(req);
        Command command = commandProvider.getCommand(commandName);
        command.execute(req, resp, servletContext);
    }

    private String getCommandName(HttpServletRequest request) {
        String commandName = null;
        String command = request.getParameter("command");
        if (command != null) {
            commandName = command;
        } else {
            commandName = request.getRequestURI().substring(1);
        }
        return commandName;
    }
}
