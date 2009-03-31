/**
 * Copyright (c) 2009 Hugo Corbucci and others.<br>
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html<br>
 * <br>
 * Contributors:<br>
 * Hugo Corbucci - initial API and implementation<br>
 * <br>
 * This file was created on 2009/01/10, 11:16:48, by Hugo Corbucci.<br>
 * It is part of package br.org.archimedes.trim on the br.org.archimedes.trims.tests project.<br>
 */
package br.org.archimedes.trim;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import br.org.archimedes.Tester;
import br.org.archimedes.exceptions.InvalidArgumentException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.infiniteline.InfiniteLine;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;
import br.org.archimedes.semiline.Semiline;
import br.org.archimedes.trims.interfaces.Trimmer;

public class InfiniteLineTrimTest extends Tester {

	Trimmer trimmer = new InfiniteLineTrimmer();

	Collection<Element> references = new ArrayList<Element>();

	@Override
	public void setUp() throws NullArgumentException, InvalidArgumentException {

		InfiniteLine biasedLine = new InfiniteLine(new Point(1.0, 4.0),
				new Point(-1.0, 0.0));
		InfiniteLine verticalXLine = new InfiniteLine(new Point(2.0, 4.0),
				new Point(2.0, 0.0));
		references.add(biasedLine);
		references.add(verticalXLine);
	}

	@Test(expected = NullArgumentException.class)
	public void testNullLineArgument() throws NullArgumentException {

		trimmer.trim(null, references, new Point(0.0, 0.0));
	}

	@Test(expected = NullArgumentException.class)
	public void testNullReferencesArgument() throws NullArgumentException,
			InvalidArgumentException {

		InfiniteLine xline3 = new InfiniteLine(new Point(-1.0, 2.0), new Point(
				3.0, 2.0));
		trimmer.trim(xline3, null, new Point(0.0, 0.0));
	}

	@Test
	public void infiniteLineTrimsCenter() throws NullArgumentException,
			InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-1.0, 2.0),
				new Point(3.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(1.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(-1.0, 2.0)));
		assertCollectionContains(collection, new Semiline(new Point(2.0, 2.0),
				new Point(3.0, 2.0)));
		Assert
				.assertEquals(
						"A trim between references should produce exactly 2 semilines.",
						2, collection.size());
	}

	@Test
	@Ignore
	// FIXME: make it work even if initial and ending points are very close.
	public void infiniteLineDefinedVerySmallTrimsCenter()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(0.5, 2.0),
				new Point(0.6, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(1.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(-1.0, 2.0)));
		assertCollectionContains(collection, new Semiline(new Point(2.0, 2.0),
				new Point(3.0, 2.0)));
		Assert
				.assertEquals(
						"A trim between references should produce exactly 2 semilines.",
						2, collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineInitialEndClickLeft()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-4.0, 2.0),
				new Point(-2.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-5.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineInitialEndClickInitial()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-4.0, 2.0),
				new Point(-2.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-4.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineInitialEndClickCenter()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-4.0, 2.0),
				new Point(-2.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-3.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineInitialEndClickEnd()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-4.0, 2.0),
				new Point(-2.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-2.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineInitialEndClickRight()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-4.0, 2.0),
				new Point(-2.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-1.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineEndInitialClickLeft()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-2.0, 2.0),
				new Point(-4.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-5.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineEndInitialClickInitial()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-2.0, 2.0),
				new Point(-4.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-4.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineEndInitialClickCenter()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-2.0, 2.0),
				new Point(-4.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-3.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineEndInitialClickEnd()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-2.0, 2.0),
				new Point(-4.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-2.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Test
	public void infiniteLineTrimsEndingPortionOfLineEndInitialClickRight()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-2.0, 2.0),
				new Point(-4.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(-1.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at the begging should produce exactly 1 semiline.", 1,
				collection.size());
	}

	@Ignore
	@Test
	public void infiniteLineTrimsLefterPartWhenClickingExactlyOnIntersectionPoint()
			throws NullArgumentException, InvalidArgumentException {

		InfiniteLine horizontalXLine = new InfiniteLine(new Point(-1.0, 2.0),
				new Point(3.0, 2.0));
		Collection<Element> collection = trimmer.trim(horizontalXLine,
				references, new Point(2.0, 2.0));

		assertCollectionContains(collection, new Semiline(new Point(0.0, 2.0),
				new Point(-1.0, 2.0)));
		assertCollectionContains(collection, new Semiline(new Point(2.0, 2.0),
				new Point(3.0, 2.0)));
		Assert.assertEquals(
				"A trim at an intersection point removes the lefter part.", 2,
				collection.size());
	}
}
