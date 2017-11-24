package net.orange.yap.machine.impl;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.YapRuntimeFactory;
import net.orange.yap.machine.code.CodeGenerator;
import net.orange.yap.machine.code.CodeParser;
import net.orange.yap.machine.code.CodeSerializer;
import net.orange.yap.machine.code.LiteralGenerator;
import net.orange.yap.machine.impl.code.CodeParserImpl;
import net.orange.yap.machine.impl.process.InterpreterImpl;
import net.orange.yap.machine.impl.rand.*;
import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.instruction.InstructionSet;

import java.util.List;
import java.util.Random;

/**
 * User: sjsmit
 * Date: 04/08/15
 * Time: 23:10
 */
public class YapRuntimeFactoryImpl implements YapRuntimeFactory {

    private final Random random;
    private CodeParser parser;
    private CodeSerializer serializer;
    private Interpreter interpreter;
    private CodeGenerator codeGenerator;

    /* Values for the default interpreter. */
    private int codeProcessorMaxElements = 256;

    /* Values for the default code generator. */
    private float codeLiteralPercentage = .1f;
    private int codeNewIntegerBound = 100;
    private float codeMutationProbability = .1f;

    /* Values for the runtime. */
    private int maximumStackDepth = 256;
    private int maximumProgramPoints = 128;
    private int maximumExecutionInstructions = 128;


    public YapRuntimeFactoryImpl() {
        this(System.currentTimeMillis());
    }

    public YapRuntimeFactoryImpl(long randomSeed) {
        this.random = new Random(randomSeed);
    }

    public void setParser(CodeParser parser) {
        this.parser = parser;
    }

    public void setSerializer(CodeSerializer serializer) {
        this.serializer = serializer;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void setCodeProcessorMaxElements(int codeProcessorMaxElements) {
        this.codeProcessorMaxElements = codeProcessorMaxElements;
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public void setCodeLiteralPercentage(float codeLiteralPercentage) {
        this.codeLiteralPercentage = codeLiteralPercentage;
    }

    public void setCodeNewIntegerBound(int codeNewIntegerBound) {
        this.codeNewIntegerBound = codeNewIntegerBound;
    }

    public void setCodeMutationProbability(float codeMutationProbability) {
        this.codeMutationProbability = codeMutationProbability;
    }

    public void setMaximumStackDepth(int maximumStackDepth) {
        this.maximumStackDepth = maximumStackDepth;
    }

    public void setMaximumProgramPoints(int maximumProgramPoints) {
        this.maximumProgramPoints = maximumProgramPoints;
    }

    public void setMaximumExecutionInstructions(int maximumExecutionInstructions) {
        this.maximumExecutionInstructions = maximumExecutionInstructions;
    }

    @Override
    public YapRuntime create() {
        if (parser == null || serializer == null) {
            CodeParserImpl impl = new CodeParserImpl();
            parser = parser == null ? impl : parser;
            serializer = serializer == null ? impl : serializer;
        }
        interpreter = interpreter == null ? new InterpreterImpl(codeProcessorMaxElements) : interpreter;
        if (codeGenerator == null) {
            LiteralGenerator literals = new LiteralGeneratorImpl(random, codeNewIntegerBound);
            List<Instruction> instructionList = InstructionSet.ALL.getValues();
            InstructionGenerator instructions = new InstructionGeneratorImpl(random, codeLiteralPercentage, literals, instructionList);
            MutationStrategy mutation = new KozaMutationImpl(random, codeMutationProbability, instructions);
            CrossOverStrategy crossover = new UltraCrossoverImpl(random);
            codeGenerator = new RandomCodeGeneratorImpl(crossover, mutation);
        }
        final YapRuntimeImpl yrt = new YapRuntimeImpl(parser, serializer, interpreter, codeGenerator);
        yrt.setMaximumStackDepth(maximumStackDepth);
        yrt.setMaximumProgramPoints(maximumProgramPoints);
        yrt.setMaximumExecutionInstructions(maximumExecutionInstructions);
        return yrt;
    }
}

