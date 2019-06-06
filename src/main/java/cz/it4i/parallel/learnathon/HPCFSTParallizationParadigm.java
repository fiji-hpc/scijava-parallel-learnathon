package cz.it4i.parallel.learnathon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.HPCFSTRPCServerRunnerUI;
import cz.it4i.parallel.runners.HPCSettings;
import lombok.Setter;

@Plugin(type = ParallelizationParadigm.class)
public class HPCFSTParallizationParadigm implements ParallelizationParadigm {

	private FSTRPCParadigm innerParadigm = new FSTRPCParadigm();

	@Setter
	public HPCSettings settings;

	private HPCFSTRPCServerRunnerUI runner;

	@Override
	public void init() {
		runner = new HPCFSTRPCServerRunnerUI(settings);
		runner.start();
	}

	@Override
	public List<CompletableFuture<Map<String, Object>>> runAllAsync(
		String commandName, List<Map<String, Object>> parameters)
	{
		return innerParadigm.runAllAsync(commandName, parameters);
	}

	@Override
	public void close() {
		runner.close();
	}

}
