package be.ceau.podcastparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;

import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <p>
 * {@link EntityResolver} implementation that serves two functions:
 * </p>
 * <ol>
 * <li>avoid downloading DTD files from external sources
 * <li>early recognition of unsupported files (eg. HTML)
 * </ol>
 */
public class QuietResolver implements XMLResolver {

	private static final Logger logger = LoggerFactory.getLogger(QuietResolver.class);

	private static final Set<String> PUBLIC_IDS_HTML = UnmodifiableSet.of(
			"-//W3C//DTD HTML 4.01//EN",
			"-//W3C//DTD HTML 4.01 Frameset//EN", 
			"-//W3C//DTD HTML 4.01 Transitional//EN",
			"-//W3C//DTD HTML 4.0 Transitional//EN", 
			"-//W3C//DTD XHTML 1.0 Frameset//EN", 
			"-//W3C//DTD XHTML 1.0 Strict//EN", 
			"-//W3C//DTD XHTML 1.0 Transitional//EN", 
			"-//W3C//DTD XHTML 1.1//EN", 
			"-//W3C//DTD XHTML Basic 1.0//EN", 
			"-//W3C//DTD XHTML Basic 1.1//EN", 
			"-//W3C//DTD XHTML+RDFa 1.0//EN", 
			"-//W3C//ELEMENTS XHTML Inline Style 1.0//EN", 
			"-//W3C//ENTITIES XHTML Datatypes 1.0//EN", 
			"-//W3C//ENTITIES XHTML Modular Framework 1.0//EN"
		);

	private static final Set<String> SYSTEM_IDS_HTML = UnmodifiableSet.of(
			"http://w3.org/TR/html4/loose.dtd",
			"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd",
			"http://www.w3.org/TR/html4/frameset.dtd",
			"http://www.w3.org/TR/html4/loose.dtd",
			"http://www.w3.org/TR/html4/strict.dtd",
			"http://www.w3.org/TR/REC-html40/loose.dtd",
			"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd",
			"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd",
			"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
			"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd",
			"http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd", 
			"http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd" 
		);

	@Override
	public InputStream resolveEntity(String publicId, String systemId, String baseURI, String namespace) throws XMLStreamException {
		if (PUBLIC_IDS_HTML.contains(publicId) || SYSTEM_IDS_HTML.contains(systemId)) {
	//		throw new XMLStreamException("the document appears to be HTML");
		}

//		logger.info("resolveEntity{} \"{}\" {} \"{}\" {} ", System.lineSeparator(), publicId, System.lineSeparator(), systemId, System.lineSeparator());
		
		return QuietSteam.INSTANCE;
	}

	public static class QuietSteam extends InputStream {

		public static final InputStream INSTANCE = new QuietSteam();

		private QuietSteam() {
		}

		@Override
		public int read() throws IOException {
			return -1;
		}

	}
	
}
