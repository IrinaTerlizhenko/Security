package by.bsu.cinemarating.tag.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by User on 24.06.2016.
 */
@SuppressWarnings("serial")
public class Pagination extends TagSupport {
    private int size;
    private boolean upper;

    private static final String UL_BEGIN = "<ul class=\"pagination\">";
    private static final String LI_BEGIN = "<li><a href=\"";
    private static final String URL = "/controller?command=change_page&num_page=";
    private static final String LAQUO_A_LI_END = "\">&laquo;</a></li>";
    private static final String LI_TOP_BEGIN = "<li id=\"top-";
    private static final String LI_BOTTOM_BEGIN = "<li id=\"bottom-";
    private static final String A_BEGIN = "\"><a href=\"";
    private static final String END = "\">";
    private static final String A_LI_END = "</a></li>";
    private static final String RAQUO_A_LI_END = "\">&raquo;</a></li>";
    private static final String UL_END = "</ul>";

    private static final String NUM_PAGE = "num_page";
    private static final int PAGE_SIZE = 10;

    public void setSize(int size) {
        this.size = size;
    }

    public void setUpper(boolean upper) {
        this.upper = upper;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Object numPageObject = pageContext.getSession().getAttribute(NUM_PAGE);
            int numPage = (numPageObject instanceof Integer) ? (int) numPageObject : Integer.parseInt((String) numPageObject);
            String contextPath = pageContext.getServletContext().getContextPath();
            JspWriter out = pageContext.getOut();
            out.write(UL_BEGIN);
            if (numPage > 0) {
                out.write(LI_BEGIN + contextPath + URL + (numPage - 1) + LAQUO_A_LI_END);
            }
            for (int index = 0; index <= size / PAGE_SIZE; index++) {
                out.write((upper ? LI_TOP_BEGIN : LI_BOTTOM_BEGIN) + index + A_BEGIN + contextPath + URL + index + END + (index + 1) + A_LI_END);
            }
            if (numPage < size / PAGE_SIZE) {
                out.write(LI_BEGIN + contextPath + URL + (numPage + 1) + RAQUO_A_LI_END);
            }
            out.write(UL_END);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
