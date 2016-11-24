package asttryout;

import org.asttryout.AAnnonymItem;
import org.asttryout.PItem;
import org.asttryout.actions.AInputActionItem;
import org.asttryout.actions.AInteractionActionItem;
import org.asttryout.actions.AOutputActionItem;
import org.asttryout.actions.ASystemActionItem;
import org.asttryout.actions.AUserActionItem;
import org.asttryout.operators.AEnableSeqOperator;
import org.asttryout.operators.AOrderIndependenceOperator;
import org.asttryout.operators.ASuspendedResumeOperator;
import org.asttryout.tasks.AAbstractTaskItem;
import org.junit.Test;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.ast.analysis.DepthFirstAnalysisAdaptorQuestion;

public class SimpleTreeTest
{

	static PItem mkItem(PItem item, String name)
	{

		item.setName(name);
		return item;
	}

	public PItem createTree()
	{
		PItem t1 = mkItem(new AAbstractTaskItem(), "Clean the House");

		AEnableSeqOperator op = new AEnableSeqOperator();

		op.getItems().add(mkItem(new AUserActionItem(), "Fetch vaccum cleaer"));
		op.getItems().add(mkItem(new AInteractionActionItem(), "Program cleaner"));
		op.getItems().add(createLevel1());
		op.getItems().add(mkItem(new ASystemActionItem(), "Store cleaner"));

		t1.setOperator(op);
		return t1;
	}

	private PItem createLevel1()
	{
		PItem item = new AAnnonymItem();
		ASuspendedResumeOperator op = new ASuspendedResumeOperator();

		op.getItems().add(createLevel11());
		op.getItems().add(createLevel12());

		item.setOperator(op);
		return item;
	}

	private PItem createLevel11()
	{
		PItem t1 = mkItem(new AAbstractTaskItem(), "Clean");

		AOrderIndependenceOperator op = new AOrderIndependenceOperator();

		op.getItems().add(mkItem(new ASystemActionItem(), "Clean Entrance"));
		op.getItems().add(mkItem(new ASystemActionItem(), "Clean Leaving Room"));
		op.getItems().add(mkItem(new ASystemActionItem(), "Clean Beadroom"));
		t1.setOperator(op);
		return t1;
	}

	private PItem createLevel12()
	{
		PItem t1 = mkItem(new AAbstractTaskItem(), "Empty Bag");

		AEnableSeqOperator op = new AEnableSeqOperator();

		op.getItems().add(mkItem(new AOutputActionItem(), "Full Bag alarm"));
		op.getItems().add(mkItem(new AInputActionItem(), "Empty Bag"));
		t1.setOperator(op);
		return t1;
	}

	@Test
	public void test() throws AnalysisException
	{
		System.out.println("\n\ntest:");
		createTree().apply(new DepthFirstAnalysisAdaptor()
		{
			@Override
			public void defaultInPItem(PItem node) throws AnalysisException
			{
				System.out.println(node.getName());
			}
		});
	}

	enum Strategy
	{
		SelectFirstOnly
	}

	@Test
	public void testWithStrategy() throws AnalysisException
	{
		System.out.println("\n\ntestWithStrategy:");
		createTree().apply(new DepthFirstAnalysisAdaptorQuestion<Strategy>()
		{
			@Override
			public void caseAEnableSeqOperator(AEnableSeqOperator node,
					Strategy question) throws AnalysisException
			{
				switch (question)
				{
					case SelectFirstOnly:
						if (!node.getItems().isEmpty())
							node.getItems().iterator().next().apply(this, question);
						break;
					default:
						super.caseAEnableSeqOperator(node, question);
						break;
				}
			}

			@Override
			public void defaultInPItem(PItem node, Strategy question)
					throws AnalysisException
			{
				System.out.println(node.getName());
			}
		}, Strategy.SelectFirstOnly);
	}
}
