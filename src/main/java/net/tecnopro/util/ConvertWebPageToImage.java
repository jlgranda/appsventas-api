/*
 * Copyright 2012  SIGCATASTROS TECNOPRO CIA. LTDA.
 *
 * Licensed under the TECNOPRO License, Version 1.0 (the "License");
 * you may not use this file or reproduce complete or any part without express autorization from TECNOPRO CIA LTDA.
 * You may obtain a copy of the License at
 *
 *     http://www.tecnopro.net
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tecnopro.util;

//Class that Converts the web page to Image
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ConvertWebPageToImage.
 */
public abstract class ConvertWebPageToImage {

	/**
	 * The Class Kit.
	 */
	static class Kit extends HTMLEditorKit  {
		
		/* (non-Javadoc)
		 * @see javax.swing.text.html.HTMLEditorKit#createDefaultDocument()
		 */
		public Document createDefaultDocument() {
			HTMLDocument doc = (HTMLDocument) super.createDefaultDocument();
			doc.setTokenThreshold(Integer.MAX_VALUE);
			doc.setAsynchronousLoadPriority(-1);
			return doc;
		}
	}

	/**
	 * Creates the.
	 *
	 * @param src the src
	 * @param width the width
	 * @param height the height
	 * @return the buffered image
	 */
	public static BufferedImage create (String src, int width, int height) {
		BufferedImage image = null;
		JEditorPane pane = new JEditorPane();
		Kit kit = new Kit();
		pane.setEditorKit(kit);
		pane.setEditable(false);
		pane.setMargin(new Insets(0,0,0,0));
		try {
			pane.setPage(src);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			Container c = new Container();
			SwingUtilities.paintComponent(g, pane, c, 0, 0, width, height);
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}


