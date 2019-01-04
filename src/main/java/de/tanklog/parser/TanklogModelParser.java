package de.tanklog.parser;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import de.tanklog.antlr4.generated.TanklogLexer;
import de.tanklog.antlr4.generated.TanklogParser;
import de.tanklog.model.TanklogModel;
import de.tanklog.model.TanklogModel.TanklogModelFactory;

public class TanklogModelParser {
	public TanklogModel parse(InputStream in) throws IOException {
		CharStream charStream = CharStreams.fromStream(in);
		TanklogLexer lexer = new TanklogLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TanklogParser parser = new TanklogParser(tokens);
		ParseTree tree = parser.document();
		TanklogParseTreeVisitor visitor = new TanklogParseTreeVisitor();
		TanklogModelFactory modelFactory = visitor.visit(tree);
		TanklogModel tanklogModel = modelFactory.build();
		return tanklogModel;
	}
}
