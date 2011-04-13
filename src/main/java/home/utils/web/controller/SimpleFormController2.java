package home.utils.web.controller;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleFormController2 extends SimpleFormController {
    // ================================
    // NON-STATIC STUFF
    private String failureView;

    // ================================
    // CONSTRUCTORS


    public SimpleFormController2() {
        // AbstractFormController sets default cache seconds to 0.
        super();
    }

    // ================================
    // GETTERS/SETTERS

    public String getFailureView() {
        return failureView;
    }

    public void setFailureView(String _failureView) {
        failureView = _failureView;
    }

    // ================================
    // METHODS

    protected ModelAndView onFailure(Throwable e, Object command, HttpServletRequest request) {
        return new ModelAndView(this.getFailureView());
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if(isFormSubmission(request)) {
            return super.handleRequestInternal(request, response);
        } else {
            Object command = null;
            try {
                command = formBackingObject(request);
            } catch (Throwable e) {
                return onFailure(e, command, request);
            }
            // New form to show: render form view.
            return showNewForm_2(command, request, response);
        }
	}

    protected ModelAndView showNewForm_2(Object command, HttpServletRequest request, HttpServletResponse response) throws Exception {

		logger.debug("Displaying new form");
		return showForm(request, response, getErrorsForNewForm_2(command, request));
	}

    protected BindException getErrorsForNewForm_2(Object command, HttpServletRequest request) throws Exception {
		// Create form-backing object for new form.
		if (command == null) {
			throw new ServletException("Form object returned by formBackingObject() must not be null");
		}
		if (!checkCommand(command)) {
			throw new ServletException("Form object returned by formBackingObject() must match commandClass");
		}

		// Bind without validation, to allow for prepopulating a form, and for
		// convenient error evaluation in views (on both first attempt and resubmit).
		ServletRequestDataBinder binder = createBinder(request, command);
		BindException errors = new BindException(binder.getBindingResult());
		if (isBindOnNewForm()) {
			logger.debug("Binding to new form");
			binder.bind(request);
			onBindOnNewForm(request, command, errors);
		}

		// Return BindException object that resulted from binding.
		return errors;
	}

    // ================================
    // LOW-LEVEL OVERRIDES

}
