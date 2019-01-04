package de.tanklog.parser;

import org.antlr.v4.runtime.RuleContext;

import de.tanklog.antlr4.generated.TanklogParser.AirfullyladenlineContext;
import de.tanklog.antlr4.generated.TanklogParser.AirhalfladenlineContext;
import de.tanklog.antlr4.generated.TanklogParser.AirsparewheellineContext;
import de.tanklog.antlr4.generated.TanklogParser.CarnamelineContext;
import de.tanklog.antlr4.generated.TanklogParser.ContentContext;
import de.tanklog.antlr4.generated.TanklogParser.DateContext;
import de.tanklog.antlr4.generated.TanklogParser.DocumentContext;
import de.tanklog.antlr4.generated.TanklogParser.EntrylineContext;
import de.tanklog.antlr4.generated.TanklogParser.EuroContext;
import de.tanklog.antlr4.generated.TanklogParser.GaragelineContext;
import de.tanklog.antlr4.generated.TanklogParser.KilometerContext;
import de.tanklog.antlr4.generated.TanklogParser.LiterContext;
import de.tanklog.antlr4.generated.TanklogParser.NotelineContext;
import de.tanklog.antlr4.generated.TanklogParser.NumberplatelineContext;
import de.tanklog.antlr4.generated.TanklogParser.OilchangelineContext;
import de.tanklog.antlr4.generated.TanklogParser.OilchangewithfilterlineContext;
import de.tanklog.antlr4.generated.TanklogParser.OilchangewithoutfilterlineContext;
import de.tanklog.antlr4.generated.TanklogParser.PurchasedetaillineContext;
import de.tanklog.antlr4.generated.TanklogParser.WooklineContext;
import de.tanklog.antlr4.generated.TanklogParserBaseVisitor;
import de.tanklog.model.TanklogModel;
import de.tanklog.model.TanklogModel.TanklogModelFactory;

public class TanklogParseTreeVisitor extends TanklogParserBaseVisitor<TanklogModelFactory> {
	private final TanklogModelFactory _factory = new TanklogModel().new TanklogModelFactory();

	@Override
	public TanklogModelFactory visitDocument(DocumentContext ctx) {
		visitChildren(ctx);
		return _factory;
	}

	@Override
	public TanklogModelFactory visitAirfullyladenline(AirfullyladenlineContext ctx) {
		ContentContext contentContext = ctx.content();
		String fullyladenAirpressure = extractFirstChildContextString(contentContext);
		_factory.setFullyladenAirpressure(fullyladenAirpressure);
		return super.visitAirfullyladenline(ctx);
	}

	@Override
	public TanklogModelFactory visitAirsparewheelline(AirsparewheellineContext ctx) {
		ContentContext contentContext = ctx.content();
		String sparewheelAirpressure = extractFirstChildContextString(contentContext);
		_factory.setSparewheelAirpressure(sparewheelAirpressure);
		return super.visitAirsparewheelline(ctx);
	}

	@Override
	public TanklogModelFactory visitAirhalfladenline(AirhalfladenlineContext ctx) {
		ContentContext contentContext = ctx.content();
		String halfladenAirpressure = extractFirstChildContextString(contentContext);
		_factory.setHalfladenAirpressure(halfladenAirpressure);
		return super.visitAirhalfladenline(ctx);
	}

	@Override
	public TanklogModelFactory visitCarnameline(CarnamelineContext ctx) {
		ContentContext contentContext = ctx.content();
		String carname = extractFirstChildContextString(contentContext);
		_factory.setCarname(carname);
		return super.visitCarnameline(ctx);
	}

	@Override
	public TanklogModelFactory visitEntryline(EntrylineContext ctx) {
		DateContext dateContext = ctx.date();
		String date = extractFirstChildContextString(dateContext);
		EuroContext priceContext = ctx.euro();
		String price = extractFirstChildContextString(priceContext);
		LiterContext literContext = ctx.liter();
		String liter = extractFirstChildContextString(literContext);
		KilometerContext kilometerContext = ctx.kilometer();
		String kilometer = extractFirstChildContextString(kilometerContext);
		_factory.setEntry(date, price, liter, kilometer);
		return super.visitEntryline(ctx);
	}

	@Override
	public TanklogModelFactory visitGarageline(GaragelineContext ctx) {
		DateContext dateContext = ctx.date();
		String date = extractFirstChildContextString(dateContext);
		ContentContext contentContext = ctx.content();
		String value = extractFirstChildContextString(contentContext);
		_factory.setGarageEntry(date, value);
		return super.visitGarageline(ctx);
	}

	@Override
	public TanklogModelFactory visitNoteline(NotelineContext ctx) {
		ContentContext contentContext = ctx.content();
		String note = extractFirstChildContextString(contentContext);
		_factory.setNote(note);
		return super.visitNoteline(ctx);
	}

	@Override
	public TanklogModelFactory visitNumberplateline(NumberplatelineContext ctx) {
		ContentContext contentContext = ctx.content();
		String numberplate = extractFirstChildContextString(contentContext);
		_factory.setNumberplate(numberplate);
		return super.visitNumberplateline(ctx);
	}

	@Override
	public TanklogModelFactory visitOilchangeline(OilchangelineContext ctx) {
		DateContext dateContext = ctx.date();
		String date = extractFirstChildContextString(dateContext);
		KilometerContext kilometerContext = ctx.kilometer();
		String kilometer = extractFirstChildContextString(kilometerContext);
		_factory.setOilchangeEntry(date, kilometer);
		return super.visitOilchangeline(ctx);
	}

	@Override
	public TanklogModelFactory visitOilchangewithoutfilterline(OilchangewithoutfilterlineContext ctx) {
		ContentContext contentContext = ctx.content();
		String oilchange = extractFirstChildContextString(contentContext);
		_factory.setOilchange(oilchange);
		return super.visitOilchangewithoutfilterline(ctx);
	}

	@Override
	public TanklogModelFactory visitOilchangewithfilterline(OilchangewithfilterlineContext ctx) {
		ContentContext contentContext = ctx.content();
		String oilchangeWithFilter = extractFirstChildContextString(contentContext);
		_factory.setOilchangeWithFilter(oilchangeWithFilter);
		return super.visitOilchangewithfilterline(ctx);
	}

	@Override
	public TanklogModelFactory visitPurchasedetailline(PurchasedetaillineContext ctx) {
		DateContext dateContext = ctx.date();
		String date = extractFirstChildContextString(dateContext);
		KilometerContext contentContext = ctx.kilometer();
		String kilometer = extractFirstChildContextString(contentContext);
		_factory.setPurchaseDetail(date, kilometer);
		return super.visitPurchasedetailline(ctx);
	}

	@Override
	public TanklogModelFactory visitWookline(WooklineContext ctx) {
		DateContext dateContext = ctx.date();
		String date = extractFirstChildContextString(dateContext);
		_factory.setWook(date);
		return super.visitWookline(ctx);
	}

	private String extractFirstChildContextString(RuleContext ctx) {
		return ctx.getChild(0).getText().trim();
	}
}
