package tech.lihx.demo.core.web.wrap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class PrintWriterWrapper extends PrintWriter {

	private final StringWriter htmlWriter;


	public PrintWriterWrapper( Writer out, StringWriter htmlWriter ) {
		super(out);
		this.htmlWriter = htmlWriter;
	}


	@Override
	public void write( char[] buf ) {
		write(buf, 0, buf.length);
	}


	@Override
	public void write( char[] buf, int off, int len ) {
		super.write(buf, off, len);
		synchronized ( lock ) {
			htmlWriter.write(buf, off, len);
		}
	}


	@Override
	public void write( int c ) {
		super.write(c);
		synchronized ( lock ) {
			htmlWriter.write(c);
		}
	}


	@Override
	public void write( String s ) {
		write(s, 0, s.length());
	}


	@Override
	public void write( String s, int off, int len ) {
		super.write(s, off, len);
		synchronized ( lock ) {
			htmlWriter.write(s, off, len);
		}
	}

}
