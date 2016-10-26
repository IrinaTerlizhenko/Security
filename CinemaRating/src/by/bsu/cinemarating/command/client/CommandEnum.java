package by.bsu.cinemarating.command.client;

import by.bsu.cinemarating.command.*;
import by.bsu.cinemarating.command.movie.*;
import by.bsu.cinemarating.command.profile.*;
import by.bsu.cinemarating.command.session.LoginCommand;
import by.bsu.cinemarating.command.session.LogoutCommand;
import by.bsu.cinemarating.command.session.RegisterCommand;

public enum CommandEnum {
    REDIRECT {
        {
            this.command = new RedirectCommand();
        }
    },
    LOGIN {
        {
            this.command = new LoginCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutCommand();
        }
    },
    CHANGE_LANGUAGE {
        {
            this.command = new ChangeLanguageCommand();
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
        }
    },
    ALL_MOVIES {
        {
            this.command = new AllMoviesCommand();
        }
    },
    TOP_MOVIES {
        {
            this.command = new TopMoviesCommand();
        }
    },
    RATE {
        {
            this.command = new RateCommand();
        }
    },
    PROFILE {
        {
            this.command = new ProfileCommand();
        }
    },
    SHOW_MOVIE {
        {
            this.command = new ShowMovieCommand();
        }
    },
    ADD_MOVIE {
        {
            this.command = new AddMovieCommand();
        }
    },
    DELETE_MOVIE {
        {
            this.command = new DeleteMovieCommand();
        }
    },
    INIT_EDIT_PROFILE {
        {
            this.command = new InitEditProfileCommand();
        }
    },
    EDIT_PROFILE {
        {
            this.command = new EditProfileCommand();
        }
    },
    DELETE_PROFILE {
        {
            this.command = new DeleteProfileCommand();
        }
    },
    INIT_BAN {
        {
            this.command = new InitBanCommand();
        }
    },
    BAN {
        {
            this.command = new BanCommand();
        }
    },
    SEARCH {
        {
            this.command = new SearchCommand();
        }
    },
    CHANGE_PAGE {
        {
            this.command = new ChangePageCommand();
        }
    },
    DELETE_REVIEW {
        {
            this.command = new DeleteReviewCommand();
        }
    },
    ALL_USERS {
        {
            this.command = new AllUsersCommand();
        }
    },
    INIT_EDIT_MOVIE {
        {
            this.command = new InitEditMovieCommand();
        }
    },
    EDIT_MOVIE {
        {
            this.command = new EditMovieCommand();
        }
    },
    RATINGS {
        {
            this.command = new RatingsCommand();
        }
    },
    REVIEWS {
        {
            this.command = new ReviewsCommand();
        }
    },
    EDIT_REVIEW {
        {
            this.command = new EditReviewCommand();
        }
    },
    EDIT_STATUS {
        {
            this.command = new EditStatusCommand();
        }
    },
    SHARE_MOVIES {
        {
            this.command = new ShareMoviesCommand();
        }
    },
    INIT_SHARE_MOVIES {
        {
            this.command = new InitShareMoviesCommand();
        }
    };
    ActionCommand command;

    /**
     * @return current ActionCommand
     */
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
