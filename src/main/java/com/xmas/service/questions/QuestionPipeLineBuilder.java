package com.xmas.service.questions;

public class QuestionPipeLineBuilder<T, V> {

    private DataSource dataSource;
    private QuestionData<V> data;
    private DataProcessor<T, V> dataProcessor;

    private QuestionPipeLineBuilder() {

    }

    public static <T, V> QuestionPipeLineBuilder<T, V> getBuilder() {
        return new QuestionPipeLineBuilder<>();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DataProcessor<T, V> getDataProcessor() {
        return dataProcessor;
    }

    public QuestionPipeLineBuilder setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public QuestionPipeLineBuilder setData(QuestionData<V> data) {
        this.data = data;
        return this;
    }

    public QuestionPipeLineBuilder setDataProcessor(DataProcessor<T, V> dataProcessor) {
        this.dataProcessor = dataProcessor;
        return this;
    }

    public Question<T> build() {
        return new DefaultQuestionImpl();
    }

    private class DefaultQuestionImpl implements Question<T> {

        @Override
        public T getAnswer() {
            return (T) dataProcessor.processData(data.evaluateData(dataSource.getData()));
        }
    }
}
