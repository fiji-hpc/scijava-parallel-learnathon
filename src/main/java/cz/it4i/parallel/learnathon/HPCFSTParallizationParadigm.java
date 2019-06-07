package cz.it4i.parallel.learnathon;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.Host;
import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.HPCFSTRPCServerRunnerUI;
import cz.it4i.parallel.runners.HPCSettings;
import lombok.Setter;

@Plugin(type = ParallelizationParadigm.class)
public class HPCFSTParallizationParadigm implements ParallelizationParadigm {

	@Parameter
	private Context context;

	private FSTRPCParadigm innerParadigm = new FSTRPCParadigm();

	@Setter
	public HPCSettings settings;

	private HPCFSTRPCServerRunnerUI runner;

	private List<CompletableFuture<?>> allFutures = new LinkedList<>();

	@Override
	public synchronized void init() {
		if (runner != null) {
			return;
		}
		runner = new HPCFSTRPCServerRunnerUI(settings);
		runner.start();
		innerParadigm.setHosts(Host.constructListFromNamesAndCores(runner.getPorts()
			.stream().map(p -> "localhost:" + p).collect(Collectors.toList()), runner
				.getNCores()));
		context.inject(innerParadigm);
		innerParadigm.init();
	}

	@Override
	public List<CompletableFuture<Map<String, Object>>> runAllAsync(
		String commandName, List<Map<String, Object>> parameters)
	{
		return registerResultList(innerParadigm.runAllAsync(commandName,
			parameters));
	}

	@Override
	public synchronized void close() {
		if (runner != null) {
			CompletableFuture.allOf(allFutures.toArray(
				new CompletableFuture[allFutures.size()])).join();
			allFutures.clear();
			runner.close();
			runner = null;
		}
	}

	private synchronized List<CompletableFuture<Map<String, Object>>>
		registerResultList(
		List<CompletableFuture<Map<String, Object>>> futures)
	{
		this.allFutures.addAll(futures);
		return futures;
	}

}
