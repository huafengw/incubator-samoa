package org.apache.samoa.learners.classifiers.ensemble;

import org.apache.samoa.instances.Instance;
import org.apache.samoa.instances.Utils;
import org.apache.samoa.learners.Model;
import org.apache.samoa.moa.core.DoubleVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2014 - 2015 Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class EnsembleModel implements Model {
    private ArrayList<Model> modelList;
    private ArrayList<Double> modelWeightList;

    public EnsembleModel() {
    }

    public EnsembleModel(ArrayList<Model> modelList, ArrayList<Double> modelWeightList) {
        this.modelList = modelList;
        this.modelWeightList = modelWeightList;
    }

    @Override
    public double[] predict(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        for (int i = 0; i < modelList.size(); i++) {
            double[] prediction = modelList.get(i).predict(inst);
            DoubleVector vote = new DoubleVector(prediction);
            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                vote.scaleValues(modelWeightList.get(i));
                combinedVote.addValues(vote);
            }
        }
        return combinedVote.getArrayCopy();
    }

    public boolean evaluate(Instance inst) {
        int trueClass = (int) inst.classValue();
        double[]  prediction = this.predict(inst);
        int predictedClass = Utils.maxIndex(prediction);
        return trueClass == predictedClass;
    }

    @Override
    public String toString() {
        return "EnsembleModel{ " +
                "number of base model: " + modelList.size() +
                ", number of weight: " + modelWeightList.size() +
                ", base model list: " + modelList +
                ", weight list: " + modelWeightList.toString() +
                " }";
    }
}
