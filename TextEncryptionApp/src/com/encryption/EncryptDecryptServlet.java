package encryption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EncryptDecryptServlet", urlPatterns = {"/encrypt", "/decrypt"})
public class EncryptDecryptServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String text = null;
        String result = null;
        
        if (path.equals("/encrypt")) {
            text = request.getParameter("textToEncrypt");
            result = EncryptionUtil.encrypt(text);
            request.setAttribute("encryptedText", result);
            request.getRequestDispatcher("encrypt.jsp").forward(request, response);
        } else if (path.equals("/decrypt")) {
            text = request.getParameter("textToDecrypt");
            result = EncryptionUtil.decrypt(text);
            request.setAttribute("decryptedText", result);
            request.getRequestDispatcher("decrypt.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
