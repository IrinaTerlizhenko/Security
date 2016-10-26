package by.bsu.cinemarating.tag.custom;

import by.bsu.cinemarating.entity.Role;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * A tag, which body will be executed if and only if the user has administrator's rights in the system.
 */
@SuppressWarnings("serial")
public class AdminTag extends TagSupport {
    private static final String ROLE = "role";

    @Override
    public int doStartTag() throws JspException {
        if ((byte) pageContext.getSession().getAttribute(ROLE) == Role.ADMIN.ordinal()) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    @Override
    public int doAfterBody() throws JspTagException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}